package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.activity.AnimationActivity
import com.mole.androidcodestudy.activity.CustomViewTestActivity
import com.mole.androidcodestudy.activity.NestedCoordinatorActivity
import com.mole.androidcodestudy.activity.SoftInputActivity
import com.mole.androidcodestudy.activity.TextViewActivity
import com.mole.androidcodestudy.adapter.PageBean
import com.mole.androidcodestudy.adapter.PagesAdapter
import com.mole.androidcodestudy.databinding.FragmentWidgetBinding
import com.mole.androidcodestudy.extension.viewBinding

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
        ).toList()
    }
}
