package com.mole.androidcodestudy.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.google.android.material.appbar.AppBarLayout

class NestedAppBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : AppBarLayout(context, attrs, defStyleAttrs) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("AppBarLayout","触发了onTouchEvent")
        return super.onTouchEvent(event)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        Log.d("AppBarLayout","触发了dispatchNestedScroll")
        return super.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow
        )
    }

}