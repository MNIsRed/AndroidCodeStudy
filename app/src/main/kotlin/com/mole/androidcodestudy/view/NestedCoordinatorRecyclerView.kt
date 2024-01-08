package com.mole.androidcodestudy.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/08
 *     desc   :
 *     fix:由于View的dispatchTouchEvent会在点击时调用stopNestedScroll，RecyclerView的stopNestedScroll会调用parent的stopNestedScroll
 *     在使用NestedCoordinatorLayout的场景下，会导致其将mNestedScrollingParentTouch置空
 *     又因为无法触发onStartNestedScroll，不能重设mNestedScrollingParentTouch。滑动RecyclerView，无法产生预期的滑动效果
 *     使用场景：
 *     在NestedCoordinatorLayout下，不会触发嵌套滑动的RecyclerView。
 *     注意：
 *     1. 由于将stopNestedScroll覆盖，需要注意这个RecyclerView不适用和NestedCoordinatorLayout滑动方向是相同方向的情况。
 *     2. 
 *     version: 1.0
 * </pre>
 */
class NestedCoordinatorRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : RecyclerView(context, attrs, defStyleAttrs) {

    override fun stopNestedScroll(type: Int) {
    }

    override fun stopNestedScroll() {
    }
}