package com.mole.androidcodestudy.activity.system

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityImplicitIntentBinding
import com.mole.androidcodestudy.extension.viewBinding

class ImplicitIntentActivity : BaseActivity() {
    private val binding: ActivityImplicitIntentBinding by viewBinding(ActivityImplicitIntentBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnOpenMain.setOnClickListener {
            val intent = Intent("custom_action_intent")
            startActivity(intent)
        }

        binding.btnOpenWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
            startActivity(intent)
        }

        binding.btnMakeCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:10086"))
            startActivity(intent)
        }

        binding.btnSendEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:example@example.com")
                putExtra(Intent.EXTRA_SUBJECT, "邮件主题")
            }
            startActivity(intent)
        }

        binding.btnShareText.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "这是要分享的文本内容")
            }
            startActivity(Intent.createChooser(intent, "分享到"))
        }

        binding.btnTestScheme.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, 
                Uri.parse("custom://com.mole.androidcodestudy:8888/Hello from ImplicitIntent"))
            startActivity(intent)
        }
    }
} 