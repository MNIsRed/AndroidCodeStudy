package com.mole.androidcodestudy.activity

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.mole.androidcodestudy.databinding.ActivitySoftinputBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/12/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class SoftInputActivity : BaseActivity(){
    private val binding by viewBinding(ActivitySoftinputBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.showSoftInput.setOnClickListener {
            softInputMethodTest()
        }
    }
    private fun softInputMethodTest() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}