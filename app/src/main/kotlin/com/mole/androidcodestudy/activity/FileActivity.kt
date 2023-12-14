package com.mole.androidcodestudy.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.mole.androidcodestudy.databinding.ActivityFileBinding
import com.mole.androidcodestudy.extension.viewBinding
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.time.LocalDateTime

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/12/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class FileActivity : BaseActivity(){
    private val binding by viewBinding(ActivityFileBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.fileButton.setOnClickListener {
            tryFileOutput()
        }
    }

    private fun tryFileOutput() {
        try {
            val output = openFileOutput("data", Context.MODE_APPEND)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.write("click Button At ${LocalDateTime.now()}\n")
                }
            }
        } catch (e: IOException) {
            Toast.makeText(applicationContext, "写入文件异常", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}