package com.mole.androidcodestudy.widget.activity

import android.os.Bundle
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityCoordinatorLayoutBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2025/01/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CoordinatorLayoutActivity : BaseActivity() {
    private val binding by viewBinding(ActivityCoordinatorLayoutBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}