package com.mole.androidcodestudy.view

import android.content.Context
import android.graphics.Outline
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.constraintlayout.widget.ConstraintLayout
import com.mole.androidcodestudy.R

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/12/06
 *     desc   :
 *     设置规格，圆角只能有一种规格
 *     四个角都是圆角：allCorner
 *     一条边圆角：topCorner等4个属性
 *     单个角：leftTopCorner等4个属性
 *
 *     没法设置多圆角是受限于setRoundRect，如果改用Path会更灵活一些，但是也比较麻烦，当前已经足够需求了。
 *     version: 1.0
 * </pre>
 */
class CornerConstraintLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttrs:Int=0, defStyleRes:Int=0) :
    ConstraintLayout(context, attrs,defStyleAttrs,defStyleRes){
    var allCornerSize :Float = 0f
    var mainDiagonalCornerSize : Float = 0f
    var subDiagonalCornerSize : Float = 0f
    var topCornerSize :Float = 0f
    var bottomCornerSize:Float = 0f
    var leftCornerSize :Float = 0f
    var rightCornerSize :Float = 0f
    var leftTopCornerSize :Float = 0f
    var leftBottomCornerSize:Float = 0f
    var rightTopCornerSize :Float = 0f
    var rightBottomCornerSize:Float = 0f
        init {
            attrs?.let {
                val typedArray = context.obtainStyledAttributes(it, R.styleable.CornerConstraintLayout,defStyleAttrs,defStyleRes)
                allCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_allCorner,0f)

                mainDiagonalCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_mainDiagonal,0f)
                subDiagonalCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_subDiagonal,0f)


                topCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_topCorner,0f)
                bottomCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_bottomCorner,0f)
                leftCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_leftCorner,0f)
                rightCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_rightCorner,0f)


                leftTopCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_leftTopCorner,0f)
                leftBottomCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_leftBottomCorner,0f)
                rightTopCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_rightTopCorner,0f)
                rightBottomCornerSize = typedArray.getDimension(R.styleable.CornerConstraintLayout_rightBottomCorner,0f)

                typedArray.recycle()
            }
        }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider(){
            override fun getOutline(view: View, outline: Outline) {
                if (allCornerSize != 0f){
                    val rect = Rect(0,0,view.width,view.height)
                    outline.setRoundRect(rect,allCornerSize)
                }else if (mainDiagonalCornerSize != 0f){
                    val rect = RectF(0f,0f,view.width.toFloat(),view.height.toFloat())
                    Path().apply {
                        addRoundRect(rect, floatArrayOf(mainDiagonalCornerSize,mainDiagonalCornerSize,0f,0f,mainDiagonalCornerSize,mainDiagonalCornerSize,0f,0f),Path.Direction.CCW)
                    }.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            outline.setPath(it)
                        }
                    }
                }else if (subDiagonalCornerSize != 0f){
                    val rect = RectF(0f,0f,view.width.toFloat(),view.height.toFloat())
                    Path().apply {
                        addRoundRect(rect, floatArrayOf(0f,0f,subDiagonalCornerSize,subDiagonalCornerSize,0f,0f,subDiagonalCornerSize,subDiagonalCornerSize),Path.Direction.CCW)
                    }.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            outline.setPath(it)
                        }
                    }
                } else if (topCornerSize != 0f){
                    val rect = Rect(0,0,view.width,view.height+topCornerSize.toInt())
                    outline.setRoundRect(rect,topCornerSize)
                }else if (bottomCornerSize != 0f){
                    val rect = Rect(0,-bottomCornerSize.toInt(),view.width,view.height)
                    outline.setRoundRect(rect,bottomCornerSize)
                }else if (leftCornerSize != 0f){
                    val rect = Rect(0,0,view.width+leftCornerSize.toInt(),view.height)
                    outline.setRoundRect(rect,leftCornerSize)
                }else if (rightCornerSize != 0f){
                    val rect = Rect(-rightCornerSize.toInt(),0,view.width,view.height)
                    outline.setRoundRect(rect,rightCornerSize)
                }else if (leftTopCornerSize != 0f){
                    val rect = Rect(0,0,view.width+leftTopCornerSize.toInt(),view.height+leftTopCornerSize.toInt())
                    outline.setRoundRect(rect,leftTopCornerSize)
                }else if (leftBottomCornerSize != 0f){
                    val rect = Rect(0,-leftBottomCornerSize.toInt(),view.width+leftBottomCornerSize.toInt(),view.height)
                    outline.setRoundRect(rect,leftBottomCornerSize)
                }else if (rightTopCornerSize != 0f){
                    val rect = Rect(-rightTopCornerSize.toInt(),0,view.width,view.height+rightTopCornerSize.toInt())
                    outline.setRoundRect(rect,rightTopCornerSize)
                }else if (rightBottomCornerSize != 0f){
                    val rect = Rect(-rightBottomCornerSize.toInt(),-rightBottomCornerSize.toInt(),view.width,view.height)
                    outline.setRoundRect(rect,rightBottomCornerSize)
                }else{
                    val rect = Rect(0,0,view.width,view.height)
                    outline.setRect(rect)
                }
            }
        }
    }
}