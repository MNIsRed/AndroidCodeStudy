package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.adapter.PageBean
import com.mole.androidcodestudy.adapter.PagesAdapter
import com.mole.androidcodestudy.databinding.FragmentWidgetBinding
import com.mole.androidcodestudy.extension.dp2px
import com.mole.androidcodestudy.extension.setSingleItemDecoration
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.home.HomeEntries
import com.mole.androidcodestudy.widget.decoration.GridSpacingItemDecoration

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
            layoutManager = GridLayoutManager(requireContext(), 2)
            setSingleItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = 2,
                    spacing = 12f.dp2px().toInt()
                )
            )
            adapter = PagesAdapter(pages)
        }
    }


    companion object {
        val pages: List<PageBean> = HomeEntries.widgetEntries.map { PageBean(it.title, it.activity) }
    }
}
