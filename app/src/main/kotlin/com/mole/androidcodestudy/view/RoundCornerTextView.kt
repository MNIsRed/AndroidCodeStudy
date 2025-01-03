package com.mole.androidcodestudy.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.mole.androidcodestudy.R
import kotlin.math.max

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/12/25
 *     desc   : 自定义圆角背景的 TextView
 *     version: 1.0
 * </pre>
 */
@Suppress("MemberVisibilityCanBePrivate")
class RoundCornerTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttrs: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttrs) {

    var backgroundSolidColor = Color.TRANSPARENT
        set(value) {
            field = value
            backgroundDrawable.setColor(value)
        }
    var backgroundStrokeColor = Color.TRANSPARENT
        set(value) {
            field = value
            backgroundDrawable.setStroke(backgroundStrokeWidth, value)
        }
    var backgroundStrokeWidth = 10
        set(value) {
            field = value
            backgroundDrawable.setStroke(value, backgroundStrokeColor)
        }
    private var backgroundDrawable: GradientDrawable = GradientDrawable()

    var allCornerRadius = 0f
        set(value) {
            field = value
            backgroundDrawable.setBackgroundCorner()
        }
    var leftTopCornerSize: Float = 0f
        set(value) {
            field = value
            backgroundDrawable.setBackgroundCorner()
        }
    var leftBottomCornerSize: Float = 0f
        set(value) {
            field = value
            backgroundDrawable.setBackgroundCorner()
        }
    var rightTopCornerSize: Float = 0f
        set(value) {
            field = value
            backgroundDrawable.setBackgroundCorner()
        }
    var rightBottomCornerSize: Float = 0f
        set(value) {
            field = value
            backgroundDrawable.setBackgroundCorner()
        }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.RoundCornerTextView, defStyleAttrs, 0)
            .use {
                backgroundSolidColor =
                    it.getColor(R.styleable.RoundCornerTextView_TextSolidColor, Color.TRANSPARENT)
                backgroundStrokeColor =
                    it.getColor(R.styleable.RoundCornerTextView_TextStrokeColor, Color.TRANSPARENT)
                backgroundStrokeWidth =
                    it.getDimensionPixelSize(R.styleable.RoundCornerTextView_TextStrokeWidth, 0)
                allCornerRadius = it.getDimension(R.styleable.RoundCornerTextView_TextAllCorner, 0f)
                leftTopCornerSize =
                    it.getDimension(R.styleable.RoundCornerTextView_TextLeftTopCorner, 0f)
                rightTopCornerSize =
                    it.getDimension(R.styleable.RoundCornerTextView_TextRightTopCorner, 0f)
                leftBottomCornerSize =
                    it.getDimension(R.styleable.RoundCornerTextView_TextLeftBottomCorner, 0f)
                rightBottomCornerSize =
                    it.getDimension(R.styleable.RoundCornerTextView_TextRightBottomCorner, 0f)
            }
        background = backgroundDrawable
    }

    private fun GradientDrawable.setBackgroundCorner() {
        setCornerRadii(
            floatArrayOf(
                max(allCornerRadius, leftTopCornerSize),
                max(allCornerRadius, leftTopCornerSize),
                max(allCornerRadius, rightTopCornerSize),
                max(allCornerRadius, rightTopCornerSize),
                max(allCornerRadius, rightBottomCornerSize),
                max(allCornerRadius, rightBottomCornerSize),
                max(allCornerRadius, leftBottomCornerSize),
                max(allCornerRadius, leftBottomCornerSize)
            )
        )
    }

}