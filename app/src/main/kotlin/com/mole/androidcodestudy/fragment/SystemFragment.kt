package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.activity.AlarmActivity
import com.mole.androidcodestudy.activity.CalendarActivity
import com.mole.androidcodestudy.activity.FileActivity
import com.mole.androidcodestudy.activity.LocationActivity
import com.mole.androidcodestudy.activity.PickMediaActivity
import com.mole.androidcodestudy.activity.system.BatteryActivity
import com.mole.androidcodestudy.activity.system.ImplicitIntentActivity
import com.mole.androidcodestudy.activity.system.MediaMetadataRetrieverActivity
import com.mole.androidcodestudy.adapter.PageBean
import com.mole.androidcodestudy.adapter.PagesAdapter
import com.mole.androidcodestudy.databinding.FragmentSystemBinding
import com.mole.androidcodestudy.extension.viewBinding

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
            adapter = PagesAdapter(pages)
        }
    }

    companion object {
        val pages: List<PageBean> = mapOf(
            "位置" to LocationActivity::class.java,
            "pickMedia" to PickMediaActivity::class.java,
            "文件" to FileActivity::class.java,
            "日历" to CalendarActivity::class.java,
            "定时器" to AlarmActivity::class.java,
            "电池信息" to BatteryActivity::class.java,
            "隐式意图" to ImplicitIntentActivity::class.java,
            "视频元信息" to MediaMetadataRetrieverActivity::class.java,
        ).toList()
    }
}