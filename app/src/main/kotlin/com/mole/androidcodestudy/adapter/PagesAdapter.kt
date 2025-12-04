package com.mole.androidcodestudy.adapter

import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.ItemMainEntryBinding
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

class PagesAdapter(private val data: List<PageBean>) : RecyclerView.Adapter<PagesAdapter.ViewHolder>() {

    data class PageStyle(
        val icon: String,
        @ColorRes val iconBackground: Int
    )

    private val styles: Map<String, PageStyle> = mapOf(
        // Widget
        "自定义View" to PageStyle("palette", R.color.icon_bg_purple),
        "嵌套Coordinator" to PageStyle("layers", R.color.icon_bg_blue),
        "动画" to PageStyle("animation", R.color.icon_bg_teal),
        "软键盘" to PageStyle("keyboard", R.color.icon_bg_indigo),
        "TextView" to PageStyle("title", R.color.icon_bg_pink),
        "MaterialButton" to PageStyle("smart_button", R.color.icon_bg_orange),
        "自定义 LayoutManager" to PageStyle("view_quilt", R.color.icon_bg_cyan),
        "CoordinatorLayout" to PageStyle("fullscreen", R.color.icon_bg_green),
        "MotionLayout" to PageStyle("auto_awesome_motion", R.color.icon_bg_lime),
        "ConstraintLayout" to PageStyle("dashboard_customize", R.color.icon_bg_red),
        "流失文本动画" to PageStyle("text_fields", R.color.icon_bg_yellow),
        "ForegroundImageView" to PageStyle("image", R.color.icon_bg_purple),
        "切换高度的 ViewPager2" to PageStyle("view_carousel", R.color.icon_bg_cyan),

        // System
        "位置" to PageStyle("place", R.color.icon_bg_green),
        "pickMedia" to PageStyle("collections", R.color.icon_bg_blue),
        "文件" to PageStyle("description", R.color.icon_bg_teal),
        "日历" to PageStyle("event", R.color.icon_bg_indigo),
        "定时器" to PageStyle("schedule", R.color.icon_bg_orange),
        "电池信息" to PageStyle("battery_charging_full", R.color.icon_bg_green),
        "隐式意图" to PageStyle("open_in_new", R.color.icon_bg_purple),
        "视频元信息" to PageStyle("smart_display", R.color.icon_bg_red),

        // Library
        "委托" to PageStyle("psychology", R.color.icon_bg_purple),
        "协程" to PageStyle("sync", R.color.icon_bg_blue),
        "LiveData" to PageStyle("favorite", R.color.icon_bg_pink),
        "ViewModel" to PageStyle("visibility", R.color.icon_bg_lime),
        "Lombok" to PageStyle("extension", R.color.icon_bg_orange),
        "崩溃分析 CrashTool" to PageStyle("bug_report", R.color.icon_bg_red),
        "palette" to PageStyle("palette", R.color.icon_bg_purple),
        "UETool" to PageStyle("build", R.color.icon_bg_teal),
        "Sqlcipher" to PageStyle("lock", R.color.icon_bg_blue),
        "motion photo" to PageStyle("movie", R.color.icon_bg_cyan),
        "OCR" to PageStyle("language", R.color.icon_bg_indigo),
        "PDF 预览" to PageStyle("picture_as_pdf", R.color.icon_bg_orange),
    )

    private val defaultStyle = PageStyle("apps", R.color.icon_bg_default)

    inner class ViewHolder(val binding: ItemMainEntryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val style = styles[item.first] ?: defaultStyle
        val context = holder.binding.root.context

        holder.binding.apply {
            title.text = item.first
            title.setTextColor(ContextCompat.getColor(context, R.color.main_text_primary))

            icon.text = style.icon
            icon.setTextColor(ContextCompat.getColor(context, android.R.color.white))

            (iconContainer.background as? GradientDrawable)?.setColor(
                ContextCompat.getColor(context, style.iconBackground)
            )

            cardContainer.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.main_card)
            )
            cardContainer.strokeColor = ContextCompat.getColor(context, R.color.main_card_stroke)

            cardContainer.setOnClickListener {
                (context as? Activity)?.start(item.second)
            }
        }
    }
}
