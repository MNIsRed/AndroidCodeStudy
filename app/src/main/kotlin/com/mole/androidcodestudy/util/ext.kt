package com.mole.androidcodestudy.util

import android.os.Build
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.mole.androidcodestudy.CustomApplication

val hasM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

@ColorInt fun Int.getColor():Int{
    return ContextCompat.getColor(CustomApplication.getInstance(),this)
}