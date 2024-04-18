package com.mole.androidcodestudy.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.map
import com.mole.androidcodestudy.databinding.ActivityLiveDataBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.viewmodel.LiveDateViewModel

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/04/18
 *     desc   :
 *     参考：https://blog.csdn.net/a1203991686/article/details/106952398
 *     测试livedata的map和switchMap
 *     version: 1.0
 * </pre>
 */
class LiveDataActivity : BaseActivity(){
    private val binding by viewBinding(ActivityLiveDataBinding::inflate)
    private val viewModel : LiveDateViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.showText.observe(this){
            binding.tvResult.text = it
        }
        binding.buttonGenerate.setOnClickListener {
            viewModel.generateNewContent(binding.etInputFirst.text.toString())
        }
        binding.switcher.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.changeShowStatus()
        }
    }
}