package com.mole.androidcodestudy.adapter

import com.mole.androidcodestudy.view.AdaptiveHeightViewPager2
import java.lang.ref.WeakReference

/**
 * 定义一个接口，强制要求Adapter实现onHeightMeasured方法
 */
interface HeightMeasurableAdapter {
    /**
     * 当子View的高度测量完成时调用此方法
     * @param position 子View的位置
     * @param height 子View的高度
     */
    fun onHeightMeasured(position: Int, height: Int)
    
    /**
     * 设置ViewPager引用，用于Adapter与ViewPager之间的直接通信
     * @param viewPager AdaptiveHeightViewPager2实例
     */
    fun setViewPager(viewPager: AdaptiveHeightViewPager2)
}