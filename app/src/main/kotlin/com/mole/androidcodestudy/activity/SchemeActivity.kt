package com.mole.androidcodestudy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.mole.androidcodestudy.R

class SchemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheme)

        val tvSchemeText = findViewById<TextView>(R.id.tvSchemeText)
        
        // 获取传递过来的数据
        intent.data?.let { uri ->
            val mText = uri.lastPathSegment
            tvSchemeText.text = mText
        }
    }
} 