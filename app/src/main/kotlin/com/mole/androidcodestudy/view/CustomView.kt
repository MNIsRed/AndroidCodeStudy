package com.mole.androidcodestudy.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.extension.dp2px
import kotlin.math.min

/**
 * 测试自定义View
 * 通过@JvmOverloads重载构造函数，效果等同于重写View的四个构造函数
 * 构造函数参数：
 * context：用于获取theme，resources等等
 * attrs：xml中设置的各种标签，比如android:text这种
 * defStyleAttr：当前主题中的属性，包含对样式资源的引用，0代表没有默认。
 * defStyleRes：样式资源的资源标识符，为View提供默认值。仅在defStyleAttr为0，或者在theme中找不到时可用。为0代表不用搜索。 和xml中设置style相似，创建时指定R.style.CustomView
 */
class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    //绘制文字需要
    private val paint = Paint()
    //指定绘制文字
    private var customText : String
    //获取文字边界textBounds
    private val bounds = Rect()
    init {
        // 获取自定义属性值的数组
        val typedArray = context.obtainStyledAttributes(attrs,
            R.styleable.CustomView,defStyleAttr,defStyleRes)
        // 获取指定自定义属性
        customText = typedArray.getString(R.styleable.CustomView_text) ?: ""
        // 释放 TypedArray 对象
        typedArray.recycle()

        paint.textSize = 15.dp2px()
    }
//    constructor(context:Context):super(context)
//    constructor(context:Context,attrs: AttributeSet?):super(context,attrs)
//    constructor(context:Context,attrs: AttributeSet?,defStyleAttr : Int):super(context,attrs,defStyleAttr)
//    constructor(context:Context,attrs: AttributeSet?,defStyleAttr : Int, defStyleRes : Int):super(context,attrs,defStyleAttr,defStyleRes)\

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(customText,0f,-paint.fontMetrics.top,paint)
    }

    /**
     * MeasureSpec代表一个32位的int值，高2位代表SpecMode，低30位代表SpecSize
     *
     * 如果不设置onMeasure，wrap_content在getDefaultSize方法中，取值和match_parent一致
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取View的width和height配置，AT_MOST == wrap_content
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        //获取match_parent情况下的测量宽高
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        paint.getTextBounds(customText,0,customText.length,bounds)
        //bounds.height不足以作为行高成为wrap_content的高度。
        val lineHeight = (paint.fontMetrics.bottom - paint.fontMetrics.top).toInt()
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(min(widthSpecSize,bounds.width()),min(heightSpecSize,lineHeight))
        } else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(min(widthSpecSize,bounds.width()),heightSpecSize)
        } else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,min(heightSpecSize,lineHeight))
        }
    }


}