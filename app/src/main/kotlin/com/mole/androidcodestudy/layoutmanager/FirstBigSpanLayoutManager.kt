package com.mole.androidcodestudy.layoutmanager

import androidx.recyclerview.widget.RecyclerView

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/12/04
 *     desc   : 竖向的，如果数量超过9个，第一个占满 2 行 2 列
 *     version: 1.0
 * </pre>
 */
class FirstBigSpanLayoutManager : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * 非自测量模式，需要根据位置决定 item 的宽高
     */
    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        if (state.itemCount == 0) {
            super.onMeasure(recycler, state, widthSpec, heightSpec)
            return
        }


        //如果有 9 个
        if (state.itemCount >= CHANGE_COUNT) {
            //    获取RecyclerView 的宽度
            //    获取 itemDecoration 的水平margin
            //    计算 横竖 1：:1 ，3个 item 的情况下，每个 item实际宽度，高度
            //  获取 itemDecoration 的竖直margin
            //    第一个的高度是 2倍数，所以前 5 个 item 等于原先的两行
            //    剩下的 item 正常排列
            //    获取一共占据多少行，算出实际高度
        } else {

        }
    }

    companion object {
        private const val CHANGE_COUNT = 9
        private const val HORIZON_COUNT = 3
    }
}
