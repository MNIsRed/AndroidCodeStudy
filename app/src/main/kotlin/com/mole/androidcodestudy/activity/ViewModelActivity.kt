package com.mole.androidcodestudy.activity

import android.os.Bundle
import com.mole.androidcodestudy.databinding.ActivityViewModelBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/11/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ViewModelActivity : BaseActivity() {
    private val binding by viewBinding(ActivityViewModelBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvFromActivity.text = "From Activity"

    }
}