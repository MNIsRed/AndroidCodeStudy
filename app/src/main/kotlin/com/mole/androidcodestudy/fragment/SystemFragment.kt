package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.adapter.PageBean
import com.mole.androidcodestudy.adapter.PagesAdapter
import com.mole.androidcodestudy.databinding.FragmentSystemBinding
import com.mole.androidcodestudy.extension.dp2px
import com.mole.androidcodestudy.extension.setSingleItemDecoration
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.home.HomeEntries
import com.mole.androidcodestudy.widget.decoration.GridSpacingItemDecoration

/**
 * @Description: 测试安卓系统功能api
 * @author: zhang
 * @date: 2024/2/16
 */
class SystemFragment : BaseFragment(R.layout.fragment_system) {
    private val binding: FragmentSystemBinding by viewBinding()

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
        val pages: List<PageBean> = HomeEntries.systemEntries.map { PageBean(it.title, it.activity) }
    }
}
