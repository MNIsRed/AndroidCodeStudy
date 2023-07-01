package com.mole.androidcodestudy.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class CustomViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes){

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }
}