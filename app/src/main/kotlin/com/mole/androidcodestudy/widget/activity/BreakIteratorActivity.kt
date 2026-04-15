package com.mole.androidcodestudy.widget.activity

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.compose.ComposeScreenUtil.w
import com.mole.androidcodestudy.compose.CustomEllipsizeText
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
        binding.composeView.setContent {
            Column {
                CustomEllipsizeText(
                    text = "峰学蔚来创始人，北京大学光华管理学院的打算客户经理看哈健康，资深升学规划师，知名教育博主。教育类畅销书作者，著有《决胜高中三年关键期》等书籍。",
                    maxLines = 2,
                    style = TextStyle(
                        fontSize = 15.sp, color = Color(0xFF333333), lineHeight = 22.sp // 增加一点行高更好看
                    ),
                    iconSize = 23.w,
                    icon = {
                        // 这里模拟图片箭头
                        Image(
                            painter = painterResource(id = R.drawable.icon_arrow_right_10),
                            contentDescription = "关闭",
                            modifier = Modifier.size(23.w)
                        )
                    })
                AnimatedText()
            }
        }
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

    // Theme.kt
    @Composable
    fun MyAppTheme(
        // 注意：系统深色模式的切换，Android 会自动去 values-night 读取，
        // 所以这里其实不需要手动写 if(darkTheme) 来区分颜色值！这是这个方案的一个巨大隐藏优势。
        content: @Composable () -> Unit
    ) {
        // 1. 从 XML 中读取语义颜色
        val primaryColor = colorResource(id = R.color.colorPrimary)
        val dividerColor = colorResource(id = R.color.black)

        // 2. 包装成 Compose 的 ColorScheme
        // 这样在 Compose 代码里，就不需要到处写 colorResource 了
        val colorScheme = lightColorScheme(
            primary = primaryColor,
            outline = dividerColor,
            // ... 其他映射
        )

        // 3. 注入 MaterialTheme
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }


}

@Immutable
data class CustomColors(
    val customColor: Color = Color(0xFF6E8FA5)
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

object AppTheme {
    val customColors: CustomColors
        @Composable
        get() = LocalCustomColors.current
}

@Composable
fun MyAppTheme(content: @Composable () -> Unit) {
    // 在这里赋值，兼顾 colorResource 以及 深色模式的切换
    val myCustomColors = CustomColors(customColor = colorResource(R.color.yellow))
    // CompositionLocalProvider 广播塔，广播 LocalCustomColors.current 为 myCustomColors
    CompositionLocalProvider(
        LocalCustomColors provides myCustomColors
    ) {
        // ... (这里的代码以及内部的所有子组件)
        content()
        Text(text = "测试自定义颜色", color = AppTheme.customColors.customColor)
    }
}