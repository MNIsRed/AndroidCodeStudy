package com.mole.androidcodestudy.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import java.lang.ref.WeakReference

/**
 * 轮播图工具类，为viewPager和RecyclerView添加 无限轮播需要的Runnable和TouchListener，viewPager还有PageChangeListener
 */
class UnLimitScrollUtil {
    companion object {
        const val normalScroll: Int = 0
        const val stopScroll: Int = -1
        const val afterHandRemove: Int = 1

        @SuppressLint("ClickableViewAccessibility")
        @JvmStatic
        fun onBindUnLimitScrollFunctionOnRecyclerview(
            dataSize: Int,
            viewReference: WeakReference<RecyclerView>
        ): Runnable? {
            viewReference.get()?.let {
                var runState = normalScroll
                var holdEndTime: Long = 0
                var lateRunnableEndTime: Long = 0
                val bannerRunnable: Runnable = object : Runnable {
                    override fun run() {
                        if (runState == normalScroll) {
                            it.adapter?.let { adapter ->
                                if (dataSize != 0) {
                                    (it.layoutManager as? LinearLayoutManager)?.apply {
                                        smoothScrollToPosition(
                                            it,
                                            null,
                                            findFirstVisibleItemPosition() + 1
                                        )
                                    }
                                }
                            }
                        }
                        //防止自动滑动前就手动滑动导致间隔过长
                        if (runState == afterHandRemove && lateRunnableEndTime != 0L) {
                            val leftTime = holdEndTime - lateRunnableEndTime
                            it.postDelayed(
                                this,
                                leftTime.coerceAtMost(BANNER_DURING.toLong())
                            )
                            runState = normalScroll
                            holdEndTime = 0
                        } else {
                            it.postDelayed(
                                this, BANNER_DURING.toLong()
                            )
                        }
                        lateRunnableEndTime = System.currentTimeMillis()
                    }
                }

                it.setOnTouchListener(OnTouchListener { _: View?, event: MotionEvent ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> runState = stopScroll
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            runState = afterHandRemove
                            holdEndTime = System.currentTimeMillis()
                        }

                        MotionEvent.ACTION_MOVE -> runState = stopScroll
                        else -> {
                        }
                    }
                    false
                })

                return bannerRunnable
            }
            return null
        }


        @SuppressLint("ClickableViewAccessibility")
        @JvmStatic
        fun onBindUnLimitScrollFunctionOnViewPager(
            dataSize: Int,
            viewReference: WeakReference<ViewPager>
        ): Runnable? {
            viewReference.get()?.let {
                var runState = normalScroll
                var holdEndTime: Long = 0
                var lateRunnableEndTime: Long = 0
                val bannerRunnable: Runnable = object : Runnable {
                    override fun run() {
                        if (runState == normalScroll) {
                            val bannerPagerAdapter = it.adapter
                            if (null != bannerPagerAdapter && 0 != dataSize) {
                                if (dataSize <= it.currentItem) {
                                    it.currentItem = 1
                                }
                                try {
                                    it.currentItem = it.currentItem + 1
                                } catch (ignored: Exception) {
                                }
                            }
                        }
                        //防止自动滑动前就手动滑动导致间隔过长
                        if (runState == afterHandRemove && lateRunnableEndTime != 0L) {
                            val leftTime = holdEndTime - lateRunnableEndTime
                            it.postDelayed(
                                this,
                                leftTime.coerceAtMost(BANNER_DURING.toLong())
                            )
                            runState = normalScroll
                            holdEndTime = 0
                        } else {
                            it.postDelayed(
                                this, BANNER_DURING.toLong()
                            )
                        }
                        lateRunnableEndTime = System.currentTimeMillis()
                    }
                }

                val onPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        var newPosition = position
                        it.adapter?.let { adapter ->
                            val realPosition =
                                BannerUtils.getRealPosition(position, dataSize)
                            if (position == 0 || position == BannerUtils.MAX_VALUE - 1) {
                                newPosition =
                                    BannerUtils.getOriginalPosition(dataSize + realPosition)
                                it.setCurrentItem(newPosition, false)
                            }
                        }

                    }

                    override fun onPageScrollStateChanged(state: Int) {}
                }

                it.setOnTouchListener(OnTouchListener { v: View?, event: MotionEvent ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> runState = stopScroll
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            runState = afterHandRemove
                            holdEndTime = System.currentTimeMillis()
                        }

                        MotionEvent.ACTION_MOVE -> runState = stopScroll
                        else -> {
                        }
                    }
                    false
                })

                it.addOnPageChangeListener(onPageChangeListener)
                return bannerRunnable
            }
            return null
        }

        const val BANNER_DURING = 500
    }
}