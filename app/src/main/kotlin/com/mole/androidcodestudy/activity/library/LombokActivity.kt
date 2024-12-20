package com.mole.androidcodestudy.activity.library

import android.os.Bundle
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.data.LombokTestBean
import com.mole.androidcodestudy.databinding.ActivityLombokBinding
import com.mole.androidcodestudy.extension.viewBindingByInflate

class LombokActivity : BaseActivity() {
    private val binding by viewBindingByInflate<ActivityLombokBinding>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bean = LombokTestBean("testString",123)
        binding.tvProperty.text = bean.toString()
    }
}