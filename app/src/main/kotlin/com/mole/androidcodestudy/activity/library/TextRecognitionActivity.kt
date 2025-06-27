package com.mole.androidcodestudy.activity.library

import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityTextRecognitionBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 *  TextRecognitionActivity
 */
class TextRecognitionActivity : BaseActivity() {
    private val binding by viewBinding(ActivityTextRecognitionBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let {
                binding.iv.load(it)
                try {
                    val image = InputImage.fromFilePath(this, it)
                    recognizer.process(image)
                        .addOnSuccessListener { visionText ->
                            binding.tvRecognitionResult.text = "识别成功，结果为：${visionText.text}"
                        }
                        .addOnFailureListener { e ->
                            binding.tvRecognitionResult.text = "识别失败：${e.message}"
                        }
                } catch (ignored: Exception) {
                }
            }
        }
        binding.bt.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

}