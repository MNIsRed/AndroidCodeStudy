package com.mole.androidcodestudy.activity

import android.os.Bundle
import android.text.style.AbsoluteSizeSpan
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.core.view.postDelayed
import androidx.core.widget.addTextChangedListener
import com.mole.androidcodestudy.databinding.ActivityTextViewBinding
import com.mole.androidcodestudy.extension.dp2px
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TextViewActivity : BaseActivity() {
    private val binding by viewBinding(ActivityTextViewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvTestBaseline.apply {
            //确定，相同TextView，一行文字，使用AbsoluteSizeSpan修改字体大小，不会导致baseline的改变
            text = buildSpannedString {
                append("基准AB")
                inSpans(AbsoluteSizeSpan(100)) {
                    append("AB")
                }
                append("g")
                inSpans(AbsoluteSizeSpan(100)) {
                    append("g")
                }
            }
        }
        binding.etInputContent.addTextChangedListener {
            binding.exampleTv.text = it
            setIntroduction()
        }
        binding.switchIncludeFontPadding.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.exampleTv.includeFontPadding = isChecked
            setIntroduction()
        }
        binding.etSpaceMultiplier.addTextChangedListener { text ->
            (text?.takeIf { it.isNotEmpty() }?.toString()?.toFloat() ?: 1f).let { multi ->
                binding.exampleTv.setLineSpacing(
                    (binding.etSpaceExtra.text?.takeIf { it.isNotEmpty() }?.toString()?.toFloat()
                        ?.toInt() ?: 0).dp2px(), multi
                )
            }
            setIntroduction()
        }

        binding.etSpaceExtra.addTextChangedListener { text ->
            (text?.takeIf { it.isNotEmpty() }?.toString()?.toInt() ?: 0).let { add ->
                binding.exampleTv.setLineSpacing(add.dp2px(),
                    (binding.etSpaceMultiplier.text?.takeIf { it.isNotEmpty() } ?: 0).toString()
                        .toFloat())
            }
            setIntroduction()
        }
    }

    private fun setIntroduction() {
        binding.introTv.postDelayed(100){
            binding.introTv.text = "$INTRODUCTION\nTextView高度为：${binding.exampleTv.height}"
        }
    }

    companion object {
        private const val INTRODUCTION =
            "20dp高 60；20dp的英文文字高度是81，如果设置 includeFontPadding 为 false，则高度是 71。"
    }
}