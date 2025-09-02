package com.mole.androidcodestudy.activity.widget

import android.os.Bundle
import com.blankj.utilcode.util.ScreenUtils
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.adapter.PhotoPagerAdapter
import com.mole.androidcodestudy.data.PhotoPagerBean
import com.mole.androidcodestudy.databinding.ActivityViewPager2AdaptiveHeightBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 *  ViewPager2AdaptiveHeight
 */
class ViewPager2AdaptiveHeightActivity : BaseActivity() {
    private val binding by viewBinding(ActivityViewPager2AdaptiveHeightBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.rootViewPager.getViewPager().apply {
            isUserInputEnabled = true
            adapter = PhotoPagerAdapter(
                listOf(
                    PhotoPagerBean(
                        "https://oss.fxwljy.com/attach/file1756720556535.jpg",
                        500, 500
                    ),
                    PhotoPagerBean(
                        "https://oss.fxwljy.com/attach/file1756720549870.jpg",
                        1279, 1852
                    ),
                    PhotoPagerBean(
                        "https://oss.fxwljy.com/attach/file1756720557320.jpg",
                        690, 1227
                    ),
                    PhotoPagerBean(
                        "https://oss.fxwljy.com/attach/file1756720556949.jpg",
                        500, 667
                    ),
                )
            ) { position, height ->
                binding.rootViewPager.onChildHeightMeasured(
                    position,
                    height
                )
            }
        }

    }
}