package com.mole.androidcodestudy.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.mole.androidcodestudy.data.PhotoPagerBean

/**
 *  PhotoPagerAdapter
 */


class PhotoPagerAdapter(
    private val images: List<PhotoPagerBean>,
    private val onHeightMeasured: (position: Int, ratio: Float) -> Unit
) : RecyclerView.Adapter<PhotoPagerAdapter.PhotoPagerViewHolder>() {
    
    companion object {
        private const val TAG = "PhotoPagerAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoPagerViewHolder {
        return PhotoPagerViewHolder(ImageView(parent.context).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
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

                            // 在这里执行你的测量逻辑
                            // 注意：此时 Drawable 的尺寸是固定的，可以直接用
                            val ratio = resultDrawable.intrinsicHeight.toFloat() / resultDrawable.intrinsicWidth
                            onHeightMeasured(actualPosition, ratio)

                        },
                        onError = { errorDrawable ->
                            // errorDrawable 是加载失败时显示的占位图
                            Log.e(TAG, "Image loading failed at position: $actualPosition")

                            setImageDrawable(errorDrawable)

                            // 图片加载失败时也通知测量，使用默认比例
                            val defaultRatio = images[actualPosition].height.toFloat() / images[actualPosition].width
                            onHeightMeasured(actualPosition, defaultRatio)
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
