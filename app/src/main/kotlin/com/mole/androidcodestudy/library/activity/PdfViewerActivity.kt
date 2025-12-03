package com.mole.androidcodestudy.library.activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityPdfViewerBinding
import com.mole.androidcodestudy.extension.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2025/03/18
 *     desc   : AndroidPdfViewer 示例，提供 URL 加载与本地文件加载两种测试方式
 *     version: 1.0
 * </pre>
 */
class PdfViewerActivity : BaseActivity() {
    private val binding by viewBinding(ActivityPdfViewerBinding::inflate)
    private val pdfUrl = "https://oss.fxwljy.com/attach/file1764210034140.pdf"
    private val localPdfFile get() = File(cacheDir, "sample_remote.pdf")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvUrl.text = pdfUrl
        binding.btnLoadUrl.setOnClickListener { loadPdfFromUrl() }
        binding.btnLoadFile.setOnClickListener { loadPdfFromFile() }
        // 默认先通过 URL 拉取一次，方便之后直接测试本地文件加载
        loadPdfFromUrl()
    }

    private fun loadPdfFromUrl() {
        binding.progressBar.isVisible = true
        binding.btnLoadUrl.isEnabled = false
        lifecycleScope.launch {
            val pdfFile = downloadPdf(pdfUrl)
            binding.progressBar.isVisible = false
            binding.btnLoadUrl.isEnabled = true
            if (pdfFile == null) {
                Toast.makeText(
                    this@PdfViewerActivity,
                    "PDF 下载失败，请稍后重试",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            renderPdf(pdfFile)
        }
    }

    private fun loadPdfFromFile() {
        if (!localPdfFile.exists()) {
            Toast.makeText(this, "本地文件不存在，请先执行 URL 加载", Toast.LENGTH_SHORT).show()
            return
        }
        renderPdf(localPdfFile)
    }

    // 下载远程 PDF 至缓存目录，避免主线程阻塞
    private suspend fun downloadPdf(url: String): File? = withContext(Dispatchers.IO) {
        runCatching {
            val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                connectTimeout = 10_000
                readTimeout = 15_000
                requestMethod = "GET"
            }
            try {
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    throw IllegalStateException("下载失败，状态码：${connection.responseCode}")
                }
                val targetFile = localPdfFile
                connection.inputStream.use { input ->
                    FileOutputStream(targetFile).use { output ->
                        input.copyTo(output)
                    }
                }
                targetFile
            } finally {
                connection.disconnect()
            }
        }.getOrElse {
            showError(it)
            null
        }
    }

    private fun renderPdf(file: File) {
        binding.pdfView.fromFile(file)
            .enableSwipe(true)
            .enableDoubletap(true)
            .spacing(8)
            .onError { showError(it) }
            .onPageError { _, t -> showError(t) }
            .load()
    }

    private fun renderPdfWithUrl(url: String) {
        binding.pdfView.fromUri(Uri.parse(url))
            .enableSwipe(true)
            .enableDoubletap(true)
            .spacing(8)
            .onError { showError(it) }
            .onPageError { _, t -> showError(t) }
            .load()
    }

    private fun showError(t: Throwable) {
        runOnUiThread {
            Toast.makeText(this, t.message ?: "未知错误", Toast.LENGTH_SHORT).show()
        }
    }
}
