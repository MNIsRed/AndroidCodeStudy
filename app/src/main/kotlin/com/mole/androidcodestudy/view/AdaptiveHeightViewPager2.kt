package com.mole.androidcodestudy.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * 一个能够根据子页面内容动态、平滑地调整自身高度的 ViewPager2 容器。
 */
class AdaptiveHeightViewPager2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs) {

    private val viewPager: ViewPager2
    private var currentPosition = 0
    private var currentPositionOffset = 0f
    // 用于存储每个位置的子 View 高度
    private val childHeights = mutableMapOf<Int, Int>()

    init {
        // 在容器内部添加一个 ViewPager2
        viewPager = ViewPager2(context)
        addView(viewPager, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        // 注册页面滑动回调
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // 保存当前的滑动状态
                currentPosition = position
                currentPositionOffset = positionOffset
                
                // positionOffset 是从 0.0 到 1.0 的滑动比例
                // 当 positionOffset 不为 0 时，说明正在滑动中
                if (positionOffset > 0 || childHeights.containsKey(position)) {
                    //  触发容器的重新测量
                    requestLayout()
                }
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                // 页面切换完成时，确保当前页面的高度正确
                ensureCurrentPageHeight()
                // 页面切换完成时，也需要重新测量以确保高度正确
                requestLayout()
            }
        })
    }

    /**
     * 对外暴露内部的 ViewPager2 实例，方便设置 adapter 等
     */
    fun getViewPager(): ViewPager2 = viewPager

    /**
     * 允许 Adapter 将子 View 的高度通知给容器
     * @param position 子 View 的位置
     * @param height 子 View 的高度
     */
    fun onChildHeightMeasured(position: Int, height: Int) {
        if (childHeights[position] != height) {
            childHeights[position] = height
            // 如果当前页面的高度变化了，也需要重新测量
            if (position == currentPosition) {
                requestLayout()
            }
            
            // 同时更新 ViewPager2 中对应位置的 View 高度
            updateChildViewHeight(position, height)
        }
    }
    
    /**
     * 确保当前页面的高度正确
     */
    private fun ensureCurrentPageHeight() {
        val currentHeight = childHeights[currentPosition]
        if (currentHeight != null && currentHeight > 0) {
            updateChildViewHeight(currentPosition, currentHeight)
        }
    }
    
    /**
     * 更新指定位置的子 View 高度
     */
    private fun updateChildViewHeight(position: Int, height: Int) {
        // 延迟执行，避免在测量过程中直接修改子View
        post {
            val recyclerView = viewPager.getChildAt(0) as? RecyclerView
            val layoutManager = recyclerView?.layoutManager
            
            // 如果当前页面正在显示，立即更新其高度
            if (layoutManager != null && recyclerView != null) {
                for (i in 0 until layoutManager.childCount) {
                    val child = layoutManager.getChildAt(i)
                    if (child != null) {
                        val childPosition = recyclerView.getChildAdapterPosition(child)
                        if (childPosition == position) {
                            val layoutParams = child.layoutParams
                            //if (layoutParams.height != height) {
                            //    layoutParams.height = height
                            //    child.layoutParams = layoutParams
                            //    child.requestLayout()
                            //}
                            layoutParams.height = RecyclerView.LayoutParams.MATCH_PARENT
                            child.layoutParams = layoutParams
                            child.requestLayout()
                            break
                        }
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 首先正常测量 ViewPager2，获取其默认高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        
        // 获取当前页面的高度，如果没有则使用测量得到的高度
        val currentHeight = childHeights[currentPosition] ?: measuredHeight
        val targetHeight = if (currentHeight > 0 && childHeights.isNotEmpty()) {
            // 如果没有滑动（positionOffset 为 0），直接使用当前页面高度
            if (currentPositionOffset == 0f) {
                currentHeight
            } else {
                // 滑动过程中，计算下一个页面的高度
                val nextPosition = currentPosition + 1
                val nextHeight = childHeights[nextPosition] ?: currentHeight
                
                // 使用线性插值计算滑动过程中的动态高度
                (currentHeight * (1 - currentPositionOffset) + nextHeight * currentPositionOffset).toInt()
            }
        } else {
            // 如果没有高度信息，使用默认测量高度
            measuredHeight
        }
        
        // 确保子页面高度与容器高度匹配
        ensureChildrenFillViewPager(targetHeight)
        
        // 设置容器的最终测量高度
        setMeasuredDimension(measuredWidth, targetHeight)
    }
    
    /**
     * 确保所有可见的子页面都填满ViewPager
     */
    private fun ensureChildrenFillViewPager(targetHeight: Int) {
        val recyclerView = viewPager.getChildAt(0) as? RecyclerView
        val layoutManager = recyclerView?.layoutManager
        
        if (layoutManager != null && recyclerView != null) {
            // 更新所有可见子View的高度
            for (i in 0 until layoutManager.childCount) {
                val child = layoutManager.getChildAt(i)
                if (child != null) {
                    val layoutParams = child.layoutParams
                    //if (layoutParams.height != targetHeight) {
                    //    layoutParams.height = targetHeight
                    //
                    //}
                    layoutParams.height = RecyclerView.LayoutParams.MATCH_PARENT
                    child.layoutParams = layoutParams
                    child.requestLayout()
                    // 强制重新测量子View
                    child.measure(
                        View.MeasureSpec.makeMeasureSpec(child.measuredWidth, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(targetHeight, View.MeasureSpec.EXACTLY)
                    )
                }
            }
        }
    }
}