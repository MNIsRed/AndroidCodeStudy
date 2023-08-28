package com.example.androidtestapplication

import android.graphics.Outline
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class RoundOutlineProvider(private val mRadius: Float) : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        val rect1 = Rect(0, 0, view.width, view.height)
        outline.setRoundRect(rect1, mRadius)
    }
}