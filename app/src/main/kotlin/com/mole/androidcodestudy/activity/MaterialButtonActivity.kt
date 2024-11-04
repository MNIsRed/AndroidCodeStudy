package com.mole.androidcodestudy.activity

import android.content.res.ColorStateList
import android.os.Bundle
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.ActivityMaterialButtonBinding
import com.mole.androidcodestudy.extension.getColor
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/11/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MaterialButtonActivity : BaseActivity() {
    private val binding by viewBinding(ActivityMaterialButtonBinding::inflate)
    private var switchColor = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.backgroundColorButton.apply {
            setBackgroundColor(R.color.masa_red.getColor(context))
        }
        binding.backgroundTintButton.apply {
            backgroundTintList = ColorStateList.valueOf(R.color.masa_red.getColor(context))
        }
        binding.defaultButton.apply {
            setOnClickListener {
                if (switchColor) {
                    binding.backgroundColorButton.apply {
                        setBackgroundColor(R.color.masa_red.getColor(context))
                    }
                    binding.backgroundTintButton.apply {
                        backgroundTintList =
                            ColorStateList.valueOf(R.color.masa_red.getColor(context))
                    }
                } else {
                    binding.backgroundColorButton.apply {
                        setBackgroundColor(R.color.colorPrimary.getColor(context))
                    }
                    binding.backgroundTintButton.apply {
                        backgroundTintList =
                            ColorStateList.valueOf(R.color.colorPrimary.getColor(context))
                    }
                }
                switchColor = !switchColor
            }
        }
    }
}