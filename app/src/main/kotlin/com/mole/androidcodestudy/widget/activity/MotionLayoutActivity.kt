package com.mole.androidcodestudy.widget.activity

import android.os.Bundle
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityMotionLayoutBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2025/01/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MotionLayoutActivity : BaseActivity() {
    private val binding by viewBinding(ActivityMotionLayoutBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root
    }
}