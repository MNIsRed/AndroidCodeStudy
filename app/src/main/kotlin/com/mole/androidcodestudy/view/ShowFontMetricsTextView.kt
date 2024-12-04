package com.mole.androidcodestudy.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/12/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ShowFontMetricsTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttrs: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttrs) {
    private val mPaint = Paint().apply {
        if (isInEditMode) {
            return@apply
        }
        //color = R.color.color_white.getColor()
        style = Paint.Style.FILL
        strokeWidth = 5f
    }

    private val topColor = Color.WHITE
    private val ascentColor = Color.GREEN
    private val baseColor = Color.RED
    private val descentColor = Color.YELLOW
    private val bottomColor = Color.MAGENTA
    private val textRect = Rect(0, 0, 0, 0)

    init {
        maxLines = 1
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode) {
            return
        }
        paint.getTextBounds("100", 0, 3, textRect)
        //top
        mPaint.color = topColor
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, mPaint)
        //ascent
        mPaint.color = ascentColor
        val ascentPosition =
            ((paint.fontMetrics.bottom - paint.fontMetrics.top) - (textRect.bottom.toFloat() - textRect.top.toFloat())) / 2
        canvas.drawLine(
            0f, ascentPosition, width.toFloat(), ascentPosition, mPaint
        )
        //descent
        mPaint.color = descentColor
        val descentPosition =
            ((paint.fontMetrics.bottom - paint.fontMetrics.top) - (textRect.bottom.toFloat() - textRect.top.toFloat())) / 2
        canvas.drawLine(
            0f, ascentPosition, width.toFloat(), ascentPosition, mPaint
        )

    }
}