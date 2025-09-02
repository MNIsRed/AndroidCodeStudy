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
    
    /**
     * 计算图片在容器中的最佳高度，与 Matrix 逻辑保持一致
     */
    private fun calculateOptimalHeight(drawable: android.graphics.drawable.Drawable, containerWidth: Int, maxHeight: Int = Int.MAX_VALUE): Int {
        val drawableWidth = drawable.intrinsicWidth.toFloat()
        val drawableHeight = drawable.intrinsicHeight.toFloat()
        
        if (drawableWidth <= 0 || drawableHeight <= 0) {
            return containerWidth // 默认正方形
        }
        
        // 计算按宽度缩放的高度
        val scaleX = containerWidth / drawableWidth
        val scaledHeight = (drawableHeight * scaleX).toInt()
        
        // 与 Matrix逻辑保持一致：如果缩放后高度超过最大值，则使用最大值
        return if (maxHeight != Int.MAX_VALUE && scaledHeight > maxHeight) {
            maxHeight
        } else {
            scaledHeight
        }
    }
    
    /**
     * 计算并设置图片的 Matrix，确保图片按正确比例显示
     */
    private fun ImageView.setupImageMatrix(drawable: android.graphics.drawable.Drawable, containerWidth: Int, containerHeight: Int) {
        val matrix = android.graphics.Matrix()
        val drawableWidth = drawable.intrinsicWidth.toFloat()
        val drawableHeight = drawable.intrinsicHeight.toFloat()
        
        if (drawableWidth > 0 && drawableHeight > 0) {
            // 计算缩放比例，让图片宽度充满容器宽度
            val scaleX = containerWidth / drawableWidth
            val scaleY = scaleX  // 保持宽高比，使用相同的缩放比例
            
            // 计算缩放后的图片尺寸
            val scaledWidth = drawableWidth * scaleX
            val scaledHeight = drawableHeight * scaleY
            
            // 检查缩放后的高度是否超过容器高度
            val finalScale = if (scaledHeight > containerHeight) {
                // 如果图片高度超过容器，则以容器高度为准
                containerHeight / drawableHeight
            } else {
                // 否则以宽度充满为准
                scaleX
            }
            
            // 设置最终的缩放比例
            matrix.setScale(finalScale, finalScale)
            
            // 重新计算缩放后的尺寸
            val finalScaledWidth = drawableWidth * finalScale
            val finalScaledHeight = drawableHeight * finalScale
            
            // 水平居中，垂直顶部对齐
            val dx = (containerWidth - finalScaledWidth) / 2f
            val dy = 0f  // 顶部对齐
            
            // 设置平移
            matrix.postTranslate(dx, dy)
            
            // 应用 Matrix
            imageMatrix = matrix
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoPagerViewHolder {
        return PhotoPagerViewHolder(ImageView(parent.context).apply {
            scaleType = ImageView.ScaleType.MATRIX // 使用 MATRIX 可以精确控制图片位置和缩放
            adjustViewBounds = false // 禁用自动调整，手动控制
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

                            // 使用与 Matrix一致的逻辑计算容器高度
                            val screenWidth = context.resources.displayMetrics.widthPixels
                            val calculatedHeight = calculateOptimalHeight(resultDrawable, screenWidth)
                            
                            Log.d(TAG, "Position: $actualPosition, ScreenWidth: $screenWidth, CalculatedHeight: $calculatedHeight")
                            
                            // 通知容器更新高度
                            onHeightMeasured(actualPosition, calculatedHeight)
                            
                            // 延迟设置图片的 Matrix，确保在容器高度变化时图片正确显示
                            post {
                                val currentWidth = width
                                val currentHeight = height
                                if (currentWidth > 0 && currentHeight > 0) {
                                    setupImageMatrix(resultDrawable, currentWidth, currentHeight)
                                }
                            }

                        },
                        onError = { errorDrawable ->
                            // errorDrawable 是加载失败时显示的占位图
                            Log.e(TAG, "Image loading failed at position: $actualPosition")

                            setImageDrawable(errorDrawable)

                            // 图片加载失败时使用默认数据计算高度
                            val screenWidth = context.resources.displayMetrics.widthPixels
                            // 使用图片数据中的尺寸信息构建一个虚拟的 Drawable
                            val imageData = images[actualPosition]
                            val virtualDrawable = object : android.graphics.drawable.Drawable() {
                                override fun draw(canvas: android.graphics.Canvas) {}
                                override fun setAlpha(alpha: Int) {}
                                override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {}
                                override fun getOpacity(): Int = android.graphics.PixelFormat.TRANSLUCENT
                                override fun getIntrinsicWidth(): Int = imageData.width
                                override fun getIntrinsicHeight(): Int = imageData.height
                            }
                            val calculatedHeight = calculateOptimalHeight(virtualDrawable, screenWidth)
                            
                            // 通知容器更新高度
                            onHeightMeasured(actualPosition, calculatedHeight)
                            
                            // 对错误图片也设置 Matrix
                            if (errorDrawable != null) {
                                post {
                                    val currentWidth = width
                                    val currentHeight = height
                                    if (currentWidth > 0 && currentHeight > 0) {
                                        setupImageMatrix(errorDrawable, currentWidth, currentHeight)
                                    }
                                }
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
