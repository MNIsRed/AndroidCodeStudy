package com.mole.androidcodestudy.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View.MeasureSpec
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.ActivityCustomViewTestBinding
import com.mole.androidcodestudy.view.CustomView

class CustomViewTestActivity : BaseActivity(){
    private lateinit var binding : ActivityCustomViewTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.jumpToSecond.setOnClickListener {
            ConfigurationChangeTestActivity.start(this)
        }

        binding.linear.addView(CustomView(this,null, R.attr.DefStyleAttr,R.style.CustomView))
        getTextViewMeasure()
    }

    /**
     * 按Android开发艺术中的方式，直接用onMeasure获取wrap_content时View的测量宽高
     */
    fun getTextViewMeasure(){
        //直接设置理论最大值：30个1
        val maxValue = (1 shl 30) - 1
        val maxMeasure = MeasureSpec.makeMeasureSpec(maxValue,MeasureSpec.AT_MOST)
        binding.textView.measure(maxMeasure,maxMeasure)
        Log.i("MainActivity_TextView","measuredWidth:${binding.textView.measuredWidth},measuredHeight:${binding.textView.measuredHeight}")
    }

    /**
     * 测试区域解码技术
     */
    fun bitmapRegionDecoderTest(){
        val rect = Rect(100,100,200,200)
        val mDecoder = BitmapRegionDecoder.newInstance("big_img.jpg",false)
        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val bitmap = mDecoder.decodeRegion(rect,options)
        binding.image.setImageBitmap(bitmap)
    }
}