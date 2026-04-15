package com.mole.androidcodestudy.compose

/**
 *  ComposeScreenUtil.kt
 */
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. 使用 object 创建单例
object ComposeScreenUtil {

    // 设计稿尺寸
    private const val DESIGN_WIDTH = 375.0
    private const val DESIGN_HEIGHT = 812.0 // 可选

    // 2. 将缩放比例作为单例的属性
    private var scaleW: Float = 1.0f
    private var scaleH: Float = 1.0f

    /**
     * 3. 提供一个公共的初始化方法
     *    这个方法必须在 Application 的 onCreate 中调用。
     */
    fun init(context: Context) {
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        // val screenHeightDp = displayMetrics.heightPixels / displayMetrics.density

        scaleW = screenWidthDp / DESIGN_WIDTH.toFloat()
        // scaleH = screenHeightDp / DESIGN_HEIGHT.toFloat()
    }

    // --- 为了让扩展函数能访问到比例，我们把它们定义在单例内部 ---
    // --- 注意：这里的扩展函数不再需要 @Composable 注解 ---

    val Int.w: Dp
        get() = (this * scaleW).dp

    val Double.w: Dp
        get() = (this * scaleW).dp

    val Int.h: Dp
        get() = (this * scaleH).dp

    val Double.h: Dp
        get() = (this * scaleH).dp

    val Int.sp: TextUnit
        get() = (this * scaleW).sp

    val Double.sp: TextUnit
        get() = (this * scaleW).sp

    val Dp.nonScaleSp: TextUnit
        @Composable get() {
            val fontScale = LocalDensity.current.fontScale
            // 将 dp 值除以字体缩放因子，再转换为 sp，就抵消了系统最终的乘法
            return (this.value / fontScale).sp
        }
}