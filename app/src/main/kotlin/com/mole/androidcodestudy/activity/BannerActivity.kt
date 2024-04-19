package com.mole.androidcodestudy.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mole.androidcodestudy.adapter.BannerAdapter
import com.mole.androidcodestudy.databinding.ActivityBannerBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.util.UnLimitScrollUtil
import java.lang.ref.WeakReference

class BannerActivity : BaseActivity() {
    private val binding by viewBinding(ActivityBannerBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.rcvBanner.apply {
            PagerSnapHelper().attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false).apply {

            }
            adapter = BannerAdapter(IMAGE)

        }
        UnLimitScrollUtil.onBindUnLimitScrollFunctionOnRecyclerview(3, WeakReference(binding.rcvBanner))
    }

    companion object{
        private val IMAGE = listOf(
            "https://images.pexels.com/photos/19946465/pexels-photo-19946465/free-photo-of-amaan.jpeg?auto=compress&cs=tinysrgb&w=800&lazy=load",
            "https://images.pexels.com/photos/16293663/pexels-photo-16293663.jpeg?auto=compress&cs=tinysrgb&w=800&lazy=load",
            "https://images.pexels.com/photos/18781624/pexels-photo-18781624.jpeg?auto=compress&cs=tinysrgb&w=800&lazy=load"

        )
    }
}