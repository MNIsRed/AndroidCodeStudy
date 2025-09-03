package com.mole.androidcodestudy.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.blankj.utilcode.util.ScreenUtils
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
    private fun calculateOptimalHeight(
        drawable: android.graphics.drawable.Drawable,
        containerWidth: Int,
        maxHeight: Int = Int.MAX_VALUE
    ): Int {
        val calculatedHeight =
            com.mole.androidcodestudy.util.ImageMatrixUtils.calculateOptimalHeight(
                drawable,
                containerWidth
            )

        // 如果设置了最大高度限制，则应用该限制
        return if (maxHeight != Int.MAX_VALUE && calculatedHeight > maxHeight) {
            maxHeight
        } else {
            calculatedHeight
        }
    }

    /**
     * 计算并设置图片的 Matrix，确保图片按正确比例显示
     */
    private fun ImageView.setupImageMatrix(
        drawable: android.graphics.drawable.Drawable,
        containerWidth: Int,
        containerHeight: Int
    ) {
        com.mole.androidcodestudy.util.ImageMatrixUtils.setupImageMatrix(
            this, drawable, containerWidth, containerHeight
        )
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
                Log.d(
                    TAG,
                    "Loading image at position: $actualPosition, URL: ${images[actualPosition].url}"
                )

                // 使用更直接的方式加载图片
                (holder.itemView as? ImageView)?.load(images[actualPosition].url)
                // 通知容器更新高度
                val calculatedHeight =
                    ScreenUtils.getScreenWidth() * images[actualPosition].height.toFloat() / images[actualPosition].width.toFloat()
                onHeightMeasured(actualPosition, calculatedHeight.toInt())

                // 延迟设置图片的 Matrix，确保在容器高度变化时图片正确显示
                post {
                    val currentWidth = width
                    val currentHeight = height
                    if (currentWidth > 0 && currentHeight > 0) {
                        (holder.itemView as? ImageView)?.drawable?.let {
                            drawable ->
                            setupImageMatrix(drawable, currentWidth, currentHeight)
                        }
                    }
                }
            } else {
                Log.w(
                    TAG,
                    "Invalid position: $position, actualPosition: $actualPosition, images.size: ${images.size}"
                )
            }
        }
    }

    class PhotoPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
