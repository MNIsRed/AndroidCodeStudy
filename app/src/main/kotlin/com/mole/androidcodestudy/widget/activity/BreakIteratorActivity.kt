package com.mole.androidcodestudy.widget.activity

import android.os.Bundle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityBreakIteratorBinding
import com.mole.androidcodestudy.extension.viewBinding
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

/**
 * <pre>
 *     author : mole
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/03/21
 *     desc   : 流式文本动画示例页面
 *     version: 1.0
 * </pre>
 */
class BreakIteratorActivity : BaseActivity() {
    private val binding by viewBinding(ActivityBreakIteratorBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.composeView.setContent { AnimatedText() }
    }

    @Composable
    private fun AnimatedText() {
        val text =
            """This text animates as though it is being typed \\uD83E\\\uDDDE\u200D♀\uFE0F \uD83D\uDD10  \uD83D\uDC69\u200D❤\uFE0F\u200D\uD83D\uDC68 \uD83D\uDC74\uD83C\uDFFD
                逐字逐句为文本添加动画效果
                这段代码会逐字符为文本添加动画效果。它会跟踪一个索引，以控制显示的文本量。显示的文本会动态更新，以仅显示当前索引之前的字符。最后，该变量会在发生变化时运行动画。
            """.trimMargin()

        // Use BreakIterator as it correctly iterates over characters regardless of how they are
        // stored, for example, some emojis are made up of multiple characters.
        // You don't want to break up an emoji as it animates, so using BreakIterator will ensure
        // this is correctly handled!
        val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }

        // Define how many milliseconds between each character should pause for. This will create the
        // illusion of an animation, as we delay the job after each character is iterated on.
        val typingDelayInMs = 10L

        var substringText by remember {
            mutableStateOf("")
        }
        LaunchedEffect(text) {
            // Initial start delay of the typing animation
            delay(1000)
            breakIterator.text = StringCharacterIterator(text)

            var nextIndex = breakIterator.next()
            // Iterate over the string, by index boundary
            while (nextIndex != BreakIterator.DONE) {
                substringText = text.subSequence(0, nextIndex).toString()
                // Go to the next logical character boundary
                nextIndex = breakIterator.next()
                delay(typingDelayInMs)
            }
        }
        Text(text = substringText)
    }
} 