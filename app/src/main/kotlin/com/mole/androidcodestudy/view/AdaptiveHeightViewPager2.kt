package com.mole.androidcodestudy.view

import android.content.Context
import android.util.AttributeSet
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
    private var nextPosition = 0
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
                // positionOffset 是从 0.0 到 1.0 的滑动比例
                // 当 positionOffset 不为 0 时，说明正在滑动中
                if (positionOffset > 0) {
                    //  触发容器的重新测量
                    requestLayout()
                }
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
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
        // 测量内部的 ViewPager2
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // --- 核心的高度计算逻辑 ---

        // 获取当前滑动位置和偏移量
        val position = viewPager.currentItem

        // 获取当前页面的高度，如果没有则为0
        val currentHeight = childHeights[position] ?: 0

        // 如果只有一个页面或者没有下一个页面的高度信息，则直接使用当前页面高度
        if (childHeights.size <= position + 1) {
            // 确保至少有一个最小高度，避免高度为0导致无法显示
            val finalHeight = if (currentHeight > 0) currentHeight else measuredHeight
            setMeasuredDimension(measuredWidth, finalHeight)
            return
        }

        // 获取下一个页面的高度
        val nextHeight = childHeights[position + 1] ?: 0

        // 使用线性插值计算滑动过程中的动态高度
        // (这里我们需要自己计算一个 positionOffset，因为 onMeasure 中拿不到)
        // 这是一个简化的计算，实际中可能需要更复杂的逻辑来获取精确的 offset
        // 幸运的是，ViewPager2 内部的 RecyclerView 可以帮助我们
        val recyclerView = viewPager.getChildAt(0) as? RecyclerView
        val layoutManager = recyclerView?.layoutManager
        var positionOffset = 0f

        // 这是一个技巧，通过视图的平移来估算偏移比例
        // 注意：这可能不是100%精确，但效果通常很好
        if (layoutManager != null && layoutManager.childCount > 0) {
            val firstChild = layoutManager.getChildAt(0)
            if (firstChild != null) {
                val measuredWidth = getMeasuredWidth()
                if (measuredWidth > 0) {
                    positionOffset = abs(firstChild.left).toFloat() / measuredWidth
                }
            }
        }

        // 线性插值计算
        val newHeight = (currentHeight * (1 - positionOffset) + nextHeight * positionOffset).toInt()

        // 确保有一个最小高度，避免高度为0导致无法显示
        val finalHeight = if (newHeight > 0) newHeight else measuredHeight
        // 设置容器的最终测量高度
        setMeasuredDimension(measuredWidth, finalHeight)
    }
}