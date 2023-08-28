package com.example.androidtestapplication

import android.graphics.Outline
import android.graphics.Path
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider

/**
 * setConvexPath在api29，25上都测试失效，预计setConvexPath无法满足预期
 */
class RoundBottomOutlineProvider(private val mRadius: Float) : ViewOutlineProvider() {
    private val path = Path()

    override fun getOutline(view: View, outline: Outline) {
        val w = view.width.toFloat()
        val h = view.height.toFloat()
        path.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, h - mRadius)
            quadTo(w, h, w - mRadius, h) // 底部圆角
            lineTo(mRadius, h)
            quadTo(0f, h, 0f, h - mRadius)
            lineTo(0f, 0f)
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            outline.setPath(path)
        } else if (w > mRadius && h > mRadius) {
            outline.setConvexPath(path)
        }
    }
}