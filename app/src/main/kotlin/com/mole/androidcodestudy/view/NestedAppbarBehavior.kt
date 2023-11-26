package com.mole.androidcodestudy.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat.SCROLL_AXIS_VERTICAL
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.yield

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/11/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class NestedAppbarBehavior @JvmOverloads constructor(context: Context, attrs: AttributeSet) : AppBarLayout.Behavior(context, attrs) {

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )

    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        Log.d("AppbarBehavior","触发了onInterceptTouchEvent")
        return super.onInterceptTouchEvent(parent, child, ev)
    }

    var lastY = 0f
    override fun onTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        Log.d("AppbarBehavior","触发了onTouchEvent")
        (parent as? NestedCoordinatorLayout)?.apply {
            if (parentCoordinatorInitialized()){
                val dy = (ev.y - lastY).toInt()
                if ((dy != 0)){
                    lastY = ev.y
                    //把TouchEvent交给parentCoordinatorLayout
                    //通过onStartNestedScroll或者onTouchEvent的方式并未达到预期效果
//                    return true
                }
            }
        }
        lastY = ev.y
        return super.onTouchEvent(parent, child, ev)
    }
}