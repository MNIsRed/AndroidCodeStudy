package com.mole.androidcodestudy.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.mole.androidcodestudy.adapter.NumberAdapter
import com.mole.androidcodestudy.adapter.RecyclerViewNestedAdapter
import com.mole.androidcodestudy.databinding.ActivityCustomLayoutManagerBinding
import com.mole.androidcodestudy.extension.setSingleItemDecoration
import com.mole.androidcodestudy.extension.viewBindingByInflate
import com.mole.androidcodestudy.layoutmanager.FirstBigSpanLayoutManager

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/12/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CustomLayoutManagerActivity : BaseActivity() {
    private val binding by viewBindingByInflate<ActivityCustomLayoutManagerBinding>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.rcvRoot.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerViewNestedAdapter(3)
            setSingleItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.set(0, 20, 0, 0)
                }
            })
        }
    }
}