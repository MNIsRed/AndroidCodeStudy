package com.mole.androidcodestudy.extension

import androidx.recyclerview.widget.RecyclerView

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/12/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
fun RecyclerView.setSingleItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
    if (itemDecorationCount == 0) {
        addItemDecoration(itemDecoration)
    }
}