package com.mole.androidcodestudy.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/11/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class NoScrollViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null) :
    ViewPager(context, attrs){

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // 不拦截这个事件
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        // 不处理这个事件
        return false
    }

    override fun executeKeyEvent(event: KeyEvent): Boolean {
        // 不响应按键事件
        return false
    }
}