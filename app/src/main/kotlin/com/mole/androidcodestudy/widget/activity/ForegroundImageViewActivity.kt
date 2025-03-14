package com.mole.androidcodestudy.widget.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.graphics.ColorUtils
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityForegroundImageViewBinding
import com.mole.androidcodestudy.extension.getColor
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2025/03/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ForegroundImageViewActivity : BaseActivity() {
    private val binding by viewBinding(ActivityForegroundImageViewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ColorDrawable的 setAlpha 是在原有的基础上进行透明度的设置，而 ColorUtils.setAlphaComponent 是重新设置透明度
        binding.ivForegroundAlpha.foreground = ColorDrawable(ColorUtils.setAlphaComponent(R.color.black.getColor(this),125))
        binding.ivForegroundAlphaOver.foreground = ColorDrawable(ColorUtils.setAlphaComponent(R.color.black.getColor(this),125)).apply {
            alpha = 125
        }
    }
}