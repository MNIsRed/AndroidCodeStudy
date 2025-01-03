package com.mole.androidcodestudy.activity.library

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityPaletteBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2025/01/02
 *     desc   : 用palette尝试调出马蜂窝的游记封面效果
 *     version: 1.0
 * </pre>
 */
class PaletteActivity : BaseActivity() {
    private val binding by viewBinding(ActivityPaletteBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivImageView.setImageResource(R.drawable.icon_palette_example)
        generatePaletteColor()
    }

    private fun generatePaletteColor() {
        try {
            ContextCompat.getDrawable(this, R.drawable.icon_palette_example)?.let {
                it.toBitmap().let { bitmap: Bitmap ->
                    //为什么取成了黑色
                    var paletteColor = Palette.Builder(bitmap)
                        .clearFilters()
                        .setRegion(10, bitmap.height / 2, bitmap.width / 2, bitmap.height - 10)
                        .generate()
                        .getDominantColor(
                            Color.TRANSPARENT
                        )
                    var paletteColor2 = Palette.Builder(bitmap)
                        .clearFilters()
                        .setRegion(
                            bitmap.width / 2,
                            bitmap.height / 2,
                            bitmap.width - 1,
                            bitmap.height - 10
                        ).generate()
                        .getDominantColor(
                            Color.TRANSPARENT
                        )

                    paletteColor = Color.argb(
                        127,
                        Color.red(paletteColor),
                        Color.green(paletteColor),
                        Color.blue(paletteColor)
                    )
                    paletteColor2 = Color.argb(
                        127,
                        Color.red(paletteColor2),
                        Color.green(paletteColor2),
                        Color.blue(paletteColor2)
                    )
                    binding.ivPaletteCover.setBackgroundColor(
                        ColorUtils.blendARGB(paletteColor, paletteColor2, 0.5f)
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("palette Error", e.cause?.toString() ?: "")
        }

    }
}