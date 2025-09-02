package com.mole.androidcodestudy.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.target.Target
import com.mole.androidcodestudy.data.PhotoPagerBean

/**
 *  PhotoPagerAdapter
 */


class PhotoPagerAdapter(
    private val images: List<PhotoPagerBean>,
    private val onHeightMeasured: (position: Int, height: Int) -> Unit
) : RecyclerView.Adapter<PhotoPagerAdapter.PhotoPagerViewHolder>() {
    
    companion object {
        private const val TAG = "PhotoPagerAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoPagerViewHolder {
        return PhotoPagerViewHolder(ImageView(parent.context).apply {
            scaleType = ImageView.ScaleType.FIT_XY
            adjustViewBounds = true
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: PhotoPagerViewHolder, position: Int) {
        (holder.itemView as? ImageView)?.apply {
            // 使用holder.bindingAdapterPosition更安全
            val actualPosition = holder.bindingAdapterPosition
            if (actualPosition != RecyclerView.NO_POSITION && actualPosition < images.size) {
                Log.d(TAG, "Loading image at position: $actualPosition, URL: ${images[actualPosition].url}")
                
                // 使用更直接的方式加载图片
                load(images[actualPosition].url) {
                    target(
                        onSuccess = { resultDrawable ->
                            // resultDrawable 就是加载成功的图片 Drawable
                            Log.d(TAG, "Image available (from cache or network) at position: $actualPosition")

                            // 将 Drawable 设置给 ImageView
                            setImageDrawable(resultDrawable)

                            // 计算实际需要的高度（基于屏幕宽度和图片宽高比）
                            val screenWidth = context.resources.displayMetrics.widthPixels
                            val ratio = resultDrawable.intrinsicHeight.toFloat() / resultDrawable.intrinsicWidth
                            val calculatedHeight = (screenWidth * ratio).toInt()
                            
                            Log.d(TAG, "Position: $actualPosition, ScreenWidth: $screenWidth, Ratio: $ratio, CalculatedHeight: $calculatedHeight")
                            
                            // 通知容器更新高度
                            onHeightMeasured(actualPosition, calculatedHeight)
                            
                            // 延迟设置 ImageView 的高度，避免与 ViewPager2 的测量过程冲突
                            post {
                                val layoutParams = this@apply.layoutParams
                                layoutParams.height = RecyclerView.LayoutParams.MATCH_PARENT
                                this@apply.layoutParams = layoutParams
                            }

                        },
                        onError = { errorDrawable ->
                            // errorDrawable 是加载失败时显示的占位图
                            Log.e(TAG, "Image loading failed at position: $actualPosition")

                            setImageDrawable(errorDrawable)

                            // 图片加载失败时也通知测量，使用默认比例计算高度
                            val screenWidth = context.resources.displayMetrics.widthPixels
                            val defaultRatio = images[actualPosition].height.toFloat() / images[actualPosition].width
                            val calculatedHeight = (screenWidth * defaultRatio).toInt()
                            
                            // 通知容器更新高度
                            onHeightMeasured(actualPosition, calculatedHeight)
                            
                            // 延迟设置 ImageView 的高度，避免与 ViewPager2 的测量过程冲突
                            post {
                                val layoutParams = this@apply.layoutParams
                                layoutParams.height = RecyclerView.LayoutParams.MATCH_PARENT
                                this@apply.layoutParams = layoutParams
                            }
                        }
                    )
                }
            } else {
                Log.w(TAG, "Invalid position: $position, actualPosition: $actualPosition, images.size: ${images.size}")
            }
        }
    }

    class PhotoPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
