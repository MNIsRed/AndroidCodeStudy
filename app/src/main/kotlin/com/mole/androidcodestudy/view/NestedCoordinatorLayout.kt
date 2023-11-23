package com.mole.androidcodestudy.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import androidx.core.view.children
import com.google.android.material.appbar.AppBarLayout

/**
 * <pre>
 * author : holdonly
 * e-mail : suliliveinchina@gmail.com
 * time   : 2023/11/16
 * desc   : copy from https://snipsave.com/user/mateuszpryczkowski/snippet/OkfTpFB1f11zdKlH0o/
 * version: 1.0
</pre> *
 */
@Suppress("unused")
class NestedCoordinatorLayout  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr), NestedScrollingChild3 {

    private var childHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)
    private lateinit var parentCoordinatorLayout : CoordinatorLayout
    init {
        isNestedScrollingEnabled = true

        var viewParent = parent
        while (viewParent != null){
            if (viewParent is CoordinatorLayout){
                parentCoordinatorLayout = viewParent
                break
            }
            viewParent = viewParent.parent
        }
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return childHelper.isNestedScrollingEnabled
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        childHelper.isNestedScrollingEnabled = enabled
    }

    override fun hasNestedScrollingParent(): Boolean {
        return childHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int,
        consumed: IntArray
    ) {
        childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,offsetInWindow, type, consumed)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return childHelper.hasNestedScrollingParent(type)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        val superResult = super.onStartNestedScroll(child, target, axes, type)
        return startNestedScroll(axes, type) || superResult
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        val superResult = super.onStartNestedScroll(child, target, nestedScrollAxes)
        return startNestedScroll(nestedScrollAxes) || superResult
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

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        dispatchNestedPreScroll(dx, dy, consumed, null, type)
        if (consumed[1] == 0) {
            super.onNestedPreScroll(target, dx, dy, consumed, type)
        }
        //向上滑动，并且已经滑倒顶了，交给父CoordinatorLayout
        if (consumed[1] == 0 && dy<0){
//            val offsetInWindow1 = IntArray(2)
            val offsetInWindow2 = IntArray(2)
//            findFirstDependency((parent as CoordinatorLayout).getDependencies(this))?.let {
//                it.getLocationInWindow(offsetInWindow1)
//            }
            findFirstDependency(this.children.toList())?.let {
                it.getLocationInWindow(offsetInWindow2)
            }
            val offsetInWindow3 = IntArray(2)
            getLocationInWindow(offsetInWindow3)

            if (offsetInWindow3[1] + dy <= offsetInWindow2[1]){
                if (this::parentCoordinatorLayout.isInitialized){
                    parentCoordinatorLayout.onNestedScroll(this,0, 0,dx, dy)
                    consumed[1] = dy
                }
            }
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        dispatchNestedPreScroll(dx, dy, consumed, null)
        if (consumed[1] == 0) {
            super.onNestedPreScroll(target, dx, dy, consumed, ViewCompat.TYPE_TOUCH)
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        super.onStopNestedScroll(target, type)
        stopNestedScroll(type)
    }

    override fun onStopNestedScroll(target: View) {
        super.onStopNestedScroll(target)
        stopNestedScroll()
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        val superResult = super.onNestedPreFling(target, velocityX, velocityY)
        return dispatchNestedPreFling(velocityX, velocityY) || superResult
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        val superResult = super.onNestedFling(target, velocityX, velocityY, consumed)
        return dispatchNestedFling(velocityX, velocityY, consumed) || superResult
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return childHelper.startNestedScroll(axes, type)
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return childHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        childHelper.stopNestedScroll()
    }

    override fun stopNestedScroll(type: Int) {
        childHelper.stopNestedScroll(type)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        @Nullable offsetInWindow: IntArray?,
        type: Int
    ): Boolean =
        childHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            type
        )

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        @Nullable offsetInWindow: IntArray?
    ): Boolean =
        childHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow
        )

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        @Nullable consumed: IntArray?,
        @Nullable offsetInWindow: IntArray?
    ): Boolean =
        childHelper.dispatchNestedPreScroll(
            dx,
            dy,
            consumed,
            offsetInWindow,
            ViewCompat.TYPE_TOUCH
        )

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        @Nullable consumed: IntArray?,
        @Nullable offsetInWindow: IntArray?,
        type: Int
    ): Boolean =
        childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean =
        childHelper.dispatchNestedPreFling(velocityX, velocityY)

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean = childHelper.dispatchNestedFling(velocityX, velocityY, consumed)

    var lastY = 0f
//    override fun onTouchEvent(ev: MotionEvent): Boolean {
//
//
//        when(ev.action){
//            MotionEvent.ACTION_DOWN->{
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
//                lastY = ev.y
//            }
//            MotionEvent.ACTION_MOVE->{
//                val offsetInWindow1 = IntArray(2)
//                if (this::parentCoordinatorLayout.isInitialized){
//                    findFirstDependency(parentCoordinatorLayout.getDependencies(this))?.getLocationInWindow(offsetInWindow1)
//                    if (offsetInWindow1[1] > -432) {
//                        val dy = lastY - ev.y
//                        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
//                        canScrollVertically(
//                            -1
//                        )
//                        ViewParentCompat.onStartNestedScroll(parentCoordinatorLayout,this,this,ViewCompat.SCROLL_AXIS_VERTICAL)
//                        dispatchNestedPreScroll(
//                            0, dy.toInt(), null, null,
//                            ViewCompat.TYPE_TOUCH
//                        )
//                    }
//                }
//
//            }
//        }
//
//        return super.onTouchEvent(ev)
//    }

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        val y = ev.y
//        when(ev.actionMasked){
//            MotionEvent.ACTION_DOWN->{
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//            MotionEvent.ACTION_MOVE->{
//                val detY = y - lastY
//                //滑到顶了，还在往上
//                val scrollOnTop = detY > 0 && !canScrollVertically(-1)
//                //滑到底部，还在往下
//                val scrollOnBottom = detY < 0 && !canScrollVertically(1)
//                if (scrollOnTop || scrollOnBottom){
//                    Log.d("Scroll","触发了滑动超出限度")
//                    parent.requestDisallowInterceptTouchEvent(false)
//                }
//            }
//        }
//        lastY = y
//        return super.dispatchTouchEvent(ev)
//
//    }
//
//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
//            super.onInterceptTouchEvent(ev)
//            return false
//        }
//        return true
//    }
}