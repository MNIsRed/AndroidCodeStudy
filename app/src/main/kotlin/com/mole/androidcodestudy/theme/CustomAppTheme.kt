package com.mole.androidcodestudy.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.mole.androidcodestudy.R

@Immutable
data class CustomAppThemeCustomColors(
    val customColor: Color = Color(0xFF6E8FA5)
)

val LocalCustomAppThemeCustomColors = staticCompositionLocalOf { CustomAppThemeCustomColors() }

object CustomAppThemeTheme {
    val customColors: CustomAppThemeCustomColors
        @Composable
        get() = LocalCustomAppThemeCustomColors.current
}

@Composable
fun CustomAppTheme(
    content: @Composable () -> Unit
) {
    val customColors = CustomAppThemeCustomColors(
        customColor = colorResource(id = R.color.CustomAppTheme_custom_color)
    )
    CompositionLocalProvider(
        LocalCustomAppThemeCustomColors provides customColors
    ) {
        content()
    }
}
