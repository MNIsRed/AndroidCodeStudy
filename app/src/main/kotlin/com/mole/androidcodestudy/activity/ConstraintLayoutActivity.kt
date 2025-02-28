package com.mole.androidcodestudy.activity

import android.os.Bundle
import com.mole.androidcodestudy.databinding.ActivityConstraintLayoutBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2025/02/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ConstraintLayoutActivity:BaseActivity() {
    private val binding by viewBinding(ActivityConstraintLayoutBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
    }
}