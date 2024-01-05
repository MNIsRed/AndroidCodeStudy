package com.mole.androidcodestudy.activity

import android.os.Bundle
import android.text.style.AbsoluteSizeSpan
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.mole.androidcodestudy.databinding.ActivityTextViewBinding
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
class TextViewActivity : BaseActivity(){
    private val binding by viewBinding(ActivityTextViewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvTestBaseline.apply {
            //确定，相同TextView，一行文字，使用AbsoluteSizeSpan修改字体大小，不会导致baseline的改变
            text = buildSpannedString {
                append("基准AB")
                inSpans(AbsoluteSizeSpan(100)){
                    append("AB")
                }
                append("g")
                inSpans(AbsoluteSizeSpan(100)){
                    append("g")
                }
            }
        }
    }
}