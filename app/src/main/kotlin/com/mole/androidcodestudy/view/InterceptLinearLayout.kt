package com.mole.androidcodestudy.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class InterceptLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
): LinearLayout(context, attrs, defStyleAttrs){
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}