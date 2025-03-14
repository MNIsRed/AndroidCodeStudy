package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.activity.AnimationActivity
import com.mole.androidcodestudy.activity.ConstraintLayoutActivity
import com.mole.androidcodestudy.activity.CustomLayoutManagerActivity
import com.mole.androidcodestudy.activity.CustomViewTestActivity
import com.mole.androidcodestudy.activity.MaterialButtonActivity
import com.mole.androidcodestudy.activity.NestedCoordinatorActivity
import com.mole.androidcodestudy.activity.SoftInputActivity
import com.mole.androidcodestudy.activity.TextViewActivity
import com.mole.androidcodestudy.adapter.PageBean
import com.mole.androidcodestudy.adapter.PagesAdapter
import com.mole.androidcodestudy.databinding.FragmentWidgetBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.widget.activity.BreakIteratorActivity
import com.mole.androidcodestudy.widget.activity.CoordinatorLayoutActivity
import com.mole.androidcodestudy.widget.activity.ForegroundImageViewActivity
import com.mole.androidcodestudy.widget.activity.MotionLayoutActivity

/**
 * @Description: AndroidUI控件测试聚合页面
 * @author: zhang
 * @date: 2024/2/14
 */
class WidgetFragment : BaseFragment(R.layout.fragment_widget) {

    private val binding: FragmentWidgetBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPages.apply {
            adapter = PagesAdapter(pages)
        }
    }


    companion object {
        val pages: List<PageBean> = mapOf(
            "自定义View" to CustomViewTestActivity::class.java,
            "嵌套Coordinator" to NestedCoordinatorActivity::class.java,
            "动画" to AnimationActivity::class.java,
            "软键盘" to SoftInputActivity::class.java,
            "TextView" to TextViewActivity::class.java,
            "MaterialButton" to MaterialButtonActivity::class.java,
            "自定义 LayoutManager" to CustomLayoutManagerActivity::class.java,
            "CoordinatorLayout" to CoordinatorLayoutActivity::class.java,
            "MotionLayout" to MotionLayoutActivity::class.java,
            "ConstraintLayout" to ConstraintLayoutActivity::class.java,
            "流失文本动画" to BreakIteratorActivity::class.java,
            "ForegroundImageView" to ForegroundImageViewActivity::class.java
        ).toList()
    }
}
