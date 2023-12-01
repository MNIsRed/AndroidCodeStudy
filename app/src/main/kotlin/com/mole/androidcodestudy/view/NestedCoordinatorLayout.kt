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
    }

    var parentOffset = 0
    var parentRange = 0
    var childOffset = 0

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        var viewParent = parent
        while (viewParent != null){
            if (viewParent is CoordinatorLayout){
                parentCoordinatorLayout = viewParent
                break
            }
            viewParent = viewParent.parent
        }

        findFirstDependency(parentCoordinatorLayout.children.toList())?.let {
            it.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
                override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                    parentOffset = verticalOffset
                    parentRange = appBarLayout?.totalScrollRange?:0
                }
            })
        }
        findFirstDependency(children.toList())?.let {
            it.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
                override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                    childOffset = verticalOffset
                }
            })
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
        if (!this::parentCoordinatorLayout.isInitialized){
            return
        }
        //向上滑动，并且已经滑倒顶了，交给父CoordinatorLayout
        //向下滑动dy<0
        if (consumed[1] == 0 && dy<0){
            //第二层Appbar是否已经滑动
            if (childOffset < 0){
                //第二层Appbar已经可以消费完全部
                if (dy >= childOffset){
                    return
                }else if(parentOffset < 0){
                    //自己先消费一部分，然后传给上一层滑动
                    val tempParentOffset = childOffset
                    onNestedScroll(this,0,0,dx,tempParentOffset)
                    consumed[1] = tempParentOffset
                    parentCoordinatorLayout.onNestedScroll(this,0, 0,dx, dy - tempParentOffset)
                    consumed[1] = dy
                }
            }else if (parentOffset < 0){
                //第二层appbar未滑动，向下滑动直接交给第一层
                parentCoordinatorLayout.onNestedScroll(this,0, 0,dx, dy)
                consumed[1] = dy
            }
        }else if (consumed[1] == 0 && dy>0){
            //第一层已经滑动到顶部
            if (parentOffset == -parentRange){
                return
            }else if (dy <= -parentOffset){
                //第一层可以全部消费
                parentCoordinatorLayout.onNestedScroll(this,0, 0,dx, dy)
                consumed[1] = dy
            }else{
                //先让第一层消费一部分
                val tempParentOffset = parentOffset
                parentCoordinatorLayout.onNestedScroll(this,0, 0,dx, -tempParentOffset)
                consumed[1] = dy + tempParentOffset
                onNestedScroll(this,0, 0,dx, dy+tempParentOffset)
                consumed[1] = dy
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
}