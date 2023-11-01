package com.mole.androidcodestudy.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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
typealias PageBean = Pair<String,Class<out AppCompatActivity>>
class PagesAdapter(private val data : List<PageBean>):RecyclerView.Adapter<ViewHolder>() {
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(TextView(parent.context).apply {
            textSize = 15f
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as? TextView)?.apply {
            text = data[position].first
            setOnClickListener {
                (context as? Activity)?.start(data[position].second)
            }
        }
    }
}