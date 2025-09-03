package com.mole.androidcodestudy.util

import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 * 图片Matrix计算工具类
 * 提供统一的图片缩放和对齐逻辑，避免代码重复
 */
object ImageMatrixUtils {
    
    /**
     * 为ImageView计算并设置Matrix，实现图片顶部对齐和自适应缩放
     * 
     * @param imageView 目标ImageView
     * @param drawable 要显示的图片Drawable
     * @param containerWidth 容器宽度
     * @param containerHeight 容器高度
     * @return 是否成功设置了Matrix
     */
    fun setupImageMatrix(
        imageView: ImageView, 
        drawable: Drawable?, 
        containerWidth: Int, 
        containerHeight: Int
    ): Boolean {
        if (drawable == null || containerWidth <= 0 || containerHeight <= 0) {
            return false
        }
        
        val matrix = Matrix()
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
            imageView.imageMatrix = matrix
            return true
        }
        
        return false
    }
    
    /**
     * 为ImageView重新计算Matrix（从ImageView本身获取容器尺寸）
     * 
     * @param imageView 目标ImageView
     * @return 是否成功设置了Matrix
     */
    fun recalculateImageMatrix(imageView: ImageView): Boolean {
        val drawable = imageView.drawable
        return if (drawable != null && imageView.width > 0 && imageView.height > 0) {
            setupImageMatrix(imageView, drawable, imageView.width, imageView.height)
        } else {
            false
        }
    }
    
    /**
     * 计算图片的最优显示高度（与Matrix计算使用相同逻辑）
     * 
     * @param drawable 图片Drawable
     * @param containerWidth 容器宽度
     * @return 计算得到的最优高度
     */
    fun calculateOptimalHeight(drawable: Drawable?, containerWidth: Int): Int {
        if (drawable == null || containerWidth <= 0) {
            return 0
        }
        
        val drawableWidth = drawable.intrinsicWidth.toFloat()
        val drawableHeight = drawable.intrinsicHeight.toFloat()
        
        if (drawableWidth > 0 && drawableHeight > 0) {
            // 使用与Matrix相同的缩放逻辑：优先让宽度充满
            val scaleX = containerWidth / drawableWidth
            val scaledHeight = drawableHeight * scaleX
            
            return scaledHeight.toInt()
        }
        
        return 0
    }
}