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

        val result =  super.onStartNestedScroll(child, target, nestedScrollAxes)
        Log.d("ParentCoordinatorLayout","ParentCoordinatorLayout是否接受滑动：${result}")
        return result
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        val result = super.onStartNestedScroll(child, target, axes, type)
        Log.d("ParentCoordinatorLayout","ParentCoordinatorLayout是否接受滑动：${result}")
        return result
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
//        else{
//            dispatchPreCheckConsume(dx, dy, consumed)
//        }
    }


    private fun dispatchPreCheckConsume(
        dx: Int,
        dy: Int,
        consumed: IntArray,
    ): Boolean {
        //向上滑动，并且已经滑倒顶了，交给父CoordinatorLayout
        //向下滑动dy<0
        if (consumed[1] == 0 && dy < 0) {
            if (appbarOffset < 0) {
                //第二层appbar未滑动，向下滑动直接交给第一层
               onNestedScroll(this, 0, 0, dx, dy)
                consumed[1] = dy
            }
        }  else if (consumed[1] == 0 && dy > 0) {
            //第一层已经滑动到顶部
            if (appbarOffset == -appbarScrollRange) {
                return false
            } else if (dy <= -appbarOffset) {
                //第一层可以全部消费
                onNestedScroll(this, 0, 0, dx, dy)
                consumed[1] = dy
            } else {
                //先让第一层消费一部分
                val tempParentOffset = appbarOffset
                onNestedScroll(this, 0, 0, dx, -tempParentOffset)
                consumed[1] = dy + tempParentOffset
            }
        }
        return true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        consumed
        if (consumed[1]!=0){
            Log.d("消费了滑动距离","：${consumed[1]}")
        }
//        else{
//            dispatchPreCheckConsume(dx, dy, consumed)
//        }
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