package com.mole.androidcodestudy.library.activity

import android.os.Bundle
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityUeToolBinding
import com.mole.androidcodestudy.extension.viewBinding
import me.ele.uetool.UETool

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2025/01/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class UEToolActivity : BaseActivity() {
    private val binding by viewBinding(ActivityUeToolBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.button.setOnClickListener {
            UETool.showUETMenu()
        }
    }
}