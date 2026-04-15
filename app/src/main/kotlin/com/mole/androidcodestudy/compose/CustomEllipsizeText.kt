package com.mole.androidcodestudy.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.compose.ComposeScreenUtil.nonScaleSp
import com.mole.androidcodestudy.compose.ComposeScreenUtil.w
import kotlin.math.max
import kotlin.math.min

private data class EllipsizeResult(
    val text: AnnotatedString, val gapWidth: androidx.compose.ui.unit.TextUnit
)

/**
 *  CustomEllipsizeText.kt
 */
@Composable
fun CustomEllipsizeText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    maxLines: Int = 2,
    iconSize: Dp = 18.w,
    icon: @Composable () -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val inlineContentId = "trailing_icon"
    val gapContentId = "trailing_gap"
    val ellipsis = "..."
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = constraints.maxWidth

        // 1. 定义占位符（即图标的大小）
        val placeholder = Placeholder(
            width = iconSize.nonScaleSp,
            height = iconSize.nonScaleSp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
        )

        // 2. 测量原始全文本
        val fullMeasureResult = remember(text, style, maxWidth, maxLines) {
            textMeasurer.measure(
                text = AnnotatedString(text),
                style = style,
                constraints = Constraints(maxWidth = maxWidth),
                maxLines = maxLines
            )
        }

        // 3. 计算最终显示的文本
        val displayResult =
            remember(text, style, maxWidth, maxLines, iconSize, density, fullMeasureResult) {
                if (fullMeasureResult.hasVisualOverflow) {
                    // 如果溢出，使用二分测量找到最大可容纳的截断点
                    val constraints = Constraints(maxWidth = maxWidth)
                    var low = 0
                    var high = text.length
                    var best = 0

                    while (low <= high) {
                        val mid = (low + high) / 2
                        val candidate = text.take(mid)
                        val candidateText = buildAnnotatedString {
                            append(candidate)
                            append(ellipsis)
                            appendInlineContent(inlineContentId)
                        }
                        val placeholderStart = candidate.length + ellipsis.length
                        val candidateMeasureResult = textMeasurer.measure(
                            text = candidateText,
                            style = style,
                            constraints = constraints,
                            maxLines = maxLines,
                            placeholders = listOf(
                                AnnotatedString.Range(
                                    item = placeholder,
                                    start = placeholderStart,
                                    end = placeholderStart + 1
                                )
                            )
                        )
                        val fits =
                            !candidateMeasureResult.hasVisualOverflow && candidateMeasureResult.lineCount <= maxLines
                        if (fits) {
                            best = mid
                            low = mid + 1
                        } else {
                            high = mid - 1
                        }
                    }

                    val finalCandidate = text.take(best)
                    val finalText = buildAnnotatedString {
                        append(finalCandidate)
                        append(ellipsis)
                        appendInlineContent(inlineContentId)
                    }
                    val finalPlaceholderStart = finalCandidate.length + ellipsis.length
                    val finalMeasureResult = textMeasurer.measure(
                        text = finalText,
                        style = style,
                        constraints = constraints,
                        maxLines = maxLines,
                        placeholders = listOf(
                            AnnotatedString.Range(
                                item = placeholder,
                                start = finalPlaceholderStart,
                                end = finalPlaceholderStart + 1
                            )
                        )
                    )
                    val lastLineIndex = min(maxLines - 1, finalMeasureResult.lineCount - 1)
                    val lineRight = finalMeasureResult.getLineRight(lastLineIndex)
                    val remainingWidthPx = maxWidth.toFloat() - lineRight
                    val gapWidthSp = with(density) {
                        max(0f, remainingWidthPx - 0.5f).toSp()
                    }
                    val resultText = buildAnnotatedString {
                        append(finalCandidate)
                        append(ellipsis)
                        if (gapWidthSp.value > 0f) {
                            appendInlineContent(gapContentId)
                        }
                        appendInlineContent(inlineContentId)
                    }
                    EllipsizeResult(resultText, gapWidthSp)
                } else {
                    // 没有溢出，直接显示原文本
                    EllipsizeResult(AnnotatedString(text), 0.sp)
                }
            }

        val displayedText = displayResult.text
        val gapWidth = displayResult.gapWidth
        // 4. 渲染
        Text(
            text = displayedText,
            modifier = Modifier.fillMaxWidth(),
            style = style,
            inlineContent = buildMap {
                if (gapWidth.value > 0f) {
                    put(
                        gapContentId, InlineTextContent(
                            placeholder = Placeholder(
                                width = gapWidth,
                                height = iconSize.nonScaleSp,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                            )
                        ) {
                            Spacer(modifier = Modifier.size(0.dp))
                        })
                }
                put(
                    inlineContentId, InlineTextContent(
                        placeholder = Placeholder(
                            width = iconSize.nonScaleSp,
                            height = iconSize.nonScaleSp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center // 关键：垂直居中
                        )
                    ) {
                        icon()
                    })
            })
    }
}

// ==========================================
//                 Preview
// ==========================================

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun PreviewCustomEllipsizeText() {
    Surface(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 场景 1: 文本非常长，触发截断 + 图标
            Text(text = "场景一：长文本截断", color = Color.Blue, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))

            CustomEllipsizeText(
                text = "峰学蔚来创始人，北京大学光华管理学院的打算客户经理看哈健康，资深升学规划师，知名教育博主。教育类畅销书作者，著有《决胜高中三年关键期》等书籍。",
                maxLines = 2,
                style = TextStyle(
                    fontSize = 15.sp, color = Color(0xFF333333), lineHeight = 22.sp // 增加一点行高更好看
                ),
                iconSize = 20.w,
                icon = {
                    // 这里模拟图片箭头
                    Image(
                        painter = painterResource(id = R.drawable.icon_arrow_right_10),
                        contentDescription = "关闭",
                        modifier = Modifier.size(20.w)
                    )
                })

            Spacer(modifier = Modifier.height(24.dp))

            // 场景 2: 文本较短，不触发截断 (图标不显示，因为逻辑里只有溢出才加图标)
            // 如果你的需求是“即使不溢出也要显示图标”，把上面的 if (overflow) else 去掉，两边都加 icon 即可
            Text(text = "场景二：文本较短（无省略号）", color = Color.Blue, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))

            CustomEllipsizeText(
                text = "这是一段很短的文本，不会触发省略号。",
                maxLines = 2,
                style = TextStyle(fontSize = 15.sp, color = Color(0xFF333333)),
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.icon_arrow_right_10),
                        contentDescription = "关闭"
                    )
                })
        }
    }
}
