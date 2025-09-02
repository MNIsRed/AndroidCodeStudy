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

    // 在容器内部添加一个 ViewPager2
    private val viewPager: ViewPager2 = ViewPager2(context)
    private var currentPosition = 0
    private var currentPositionOffset = 0f
    // 用于存储每个位置的子 View 高度
    private val childHeights = mutableMapOf<Int, Int>()

    init {
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
        
        // 如果目标高度与当前测量的高度不同，需要重新测量ViewPager2
        if (targetHeight != measuredHeight) {
            // 重新测量 ViewPager2，让它适应新的高度
            val newHeightSpec = MeasureSpec.makeMeasureSpec(targetHeight, MeasureSpec.EXACTLY)
            viewPager.measure(widthMeasureSpec, newHeightSpec)
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
        
        if (layoutManager != null) {
            // 更新所有可见子View的高度
            for (i in 0 until layoutManager.childCount) {
                val child = layoutManager.getChildAt(i)
                if (child != null) {
                    //child.requestLayout()
                    // 强制重新测量子View
                    child.measure(
                        MeasureSpec.makeMeasureSpec(child.measuredWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(targetHeight, MeasureSpec.EXACTLY)
                    )
                    
                    // 如果子View是 ImageView，通知它重新计算 Matrix
                    if (child is android.widget.ImageView) {
                        child.post {
                            notifyImageViewToRecalculateMatrix(child)
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 通知 ImageView 重新计算其 Matrix
     */
    private fun notifyImageViewToRecalculateMatrix(imageView: android.widget.ImageView) {
        com.mole.androidcodestudy.util.ImageMatrixUtils.recalculateImageMatrix(imageView)
    }
}