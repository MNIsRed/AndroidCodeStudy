package com.mole.androidcodestudy.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ParentApparBehavior @JvmOverloads constructor(context: Context, attrs: AttributeSet) :
    AppBarLayout.Behavior(context, attrs) {
    override fun onAttachedToLayoutParams(params: CoordinatorLayout.LayoutParams) {
        super.onAttachedToLayoutParams(params)
    }

    var appbarOffset = 0
    var appbarScrollRange = 0

    private val listener = object : AppBarLayout.OnOffsetChangedListener {
        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            appbarScrollRange = appBarLayout.totalScrollRange
            appbarOffset = verticalOffset
        }

    }

    var lastY = 0f
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        child.addOnOffsetChangedListener(listener)
        val suerReturn = super.onInterceptTouchEvent(parent, child, ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastY = ev.y
                return suerReturn
            }

            MotionEvent.ACTION_MOVE -> {
                val dev = ev.y - lastY
                if (dev < 0 && (appbarOffset <= 0) && (-appbarOffset < child.totalScrollRange)) {
                    lastY = ev.y
                    resetActivityPointerId(ev)
                    return true
                } else if (dev > 0 && (appbarOffset < 0) && (-appbarOffset <= child.totalScrollRange)) {
                    lastY = ev.y
                    resetActivityPointerId(ev)
                    return true
                }
                lastY = ev.y
            }
        }
        Log.d("ParentApparBehavior", "触发了ParentApparBehavior的onInterceptTouchEvent")
        return suerReturn
    }

    private fun resetActivityPointerId(ev: MotionEvent) {
        val newActivePointerId = ev.getPointerId(0)
        val parentClass = ParentApparBehavior::class.java
        val field = parentClass.superclass.superclass.superclass.getDeclaredField("activePointerId")
        field.isAccessible = true
        field.set(this, newActivePointerId)
    }

    override fun onTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        Log.d("ParentApparBehavior", "触发了ParentApparBehavior的onTouchEvent")
        return super.onTouchEvent(parent, child, ev)
    }
}