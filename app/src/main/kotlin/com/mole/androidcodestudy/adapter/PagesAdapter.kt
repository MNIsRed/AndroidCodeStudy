package com.mole.androidcodestudy.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.button.MaterialButton
import com.mole.androidcodestudy.extension.dp2px
import com.mole.androidcodestudy.extension.start

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/11/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
typealias PageBean = Pair<String, Class<out AppCompatActivity>>

inline fun <reified T : AppCompatActivity> pageBean(title: String): PageBean {
    return PageBean(title, T::class.java)
}

class PagesAdapter(private val data: List<PageBean>) : RecyclerView.Adapter<ViewHolder>() {

    private val horizonDivide = 15f.dp2px().toInt()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(MaterialButton(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = horizonDivide
                marginEnd = horizonDivide
            }
            textSize = 15f
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as? MaterialButton)?.apply {
            text = data[position].first
            setOnClickListener {
                (context as? Activity)?.start(data[position].second)
            }
        }
    }
}