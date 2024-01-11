package com.mole.androidcodestudy.activity

import android.os.Bundle
import com.mole.androidcodestudy.databinding.ActivityCoroutineBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.extension.viewModelProvider
import com.mole.androidcodestudy.viewmodel.CoroutineViewModel

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/11
 *     desc   : 协程相关特性
 *     version: 1.0
 * </pre>
 */
class CoroutineActivity : BaseActivity(){
    private val binding by viewBinding(ActivityCoroutineBinding::inflate)
    private val viewModel : CoroutineViewModel by viewModelProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}