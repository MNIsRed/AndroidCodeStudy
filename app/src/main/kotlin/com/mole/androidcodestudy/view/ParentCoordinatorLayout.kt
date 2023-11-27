package com.mole.androidcodestudy.view

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import com.google.android.material.appbar.AppBarLayout

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/11/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ParentCoordinatorLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr){

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return super.onStartNestedScroll(child, target, nestedScrollAxes)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return super.onStartNestedScroll(child, target, axes, type)
    }

    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes)
        child
    }

    override fun onNestedScrollAccepted(
        child: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes, type)
        child
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)
        consumed
        if (consumed[1]!=0){
            Log.d("消费了滑动距离","：${consumed[1]}")
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        consumed
        if (consumed[1]!=0){
            Log.d("消费了滑动距离","：${consumed[1]}")
        }
    }

    fun findFirstDependency(views: List<View?>): AppBarLayout? {
        var i = 0
        val z = views.size
        while (i < z) {
            val view = views[i]
            if (view is AppBarLayout) {
                return view
            }
            i++
        }
        return null
    }
    var appbarOffset = 0
    var appbarScrollRange = 0

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findFirstDependency(children.toList())?.let{
            it.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    appbarScrollRange = appBarLayout.totalScrollRange
                    appbarOffset = verticalOffset
                }

            })
        }
    }
    var lastY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        val dev = ev.y - lastY
//        if (dev < 0 && (appbarOffset >=0) && (-appbarOffset <appbarScrollRange)){
//            lastY = ev.y
//            return true
//        }else if(dev > 0 && (appbarOffset >0) && (-appbarOffset <=appbarScrollRange)){
//            lastY = ev.y
//            return true
//        }
//        lastY = ev.y
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("ParentCoordinator","触发ParentCoordinatorLayout的onTouchEvent")
        return super.onTouchEvent(ev)
    }
}