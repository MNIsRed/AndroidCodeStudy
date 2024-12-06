package com.mole.androidcodestudy.adapter

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mole.androidcodestudy.extension.setSingleItemDecoration
import com.mole.androidcodestudy.layoutmanager.FirstBigSpanLayoutManager

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/12/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RecyclerViewNestedAdapter(private val count: Int = 0) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ViewHolder(RecyclerView(p0.context).apply {
            setPadding(10, 10, 0, 0)
            layoutManager = FirstBigSpanLayoutManager()
            setSingleItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.set(0, 0, 10, 10)
                }
            })
        })
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as? RecyclerView)?.apply {
            adapter = NumberAdapter(position + 9)
        }
    }
}