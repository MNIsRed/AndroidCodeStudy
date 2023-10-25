package com.mole.androidcodestudy.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.ActivityKotlinDelegateBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.extension.viewBindingByInflate

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/10/25
 *     desc   : 通过viewBinding这个扩展函数，实现绑定ActivityKotlinDelegateBinding
 *     version: 1.0
 * </pre>
 */
class KotlinDelegateActivity : AppCompatActivity(){
//    private val binding : ActivityKotlinDelegateBinding by viewBindingByInflate()
    private val binding by viewBinding(ActivityKotlinDelegateBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, KotlinDelegateActivity::class.java)
            context.startActivity(starter)
        }
    }
}