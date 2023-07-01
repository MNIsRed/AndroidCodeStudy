package com.mole.androidcodestudy.util

import android.graphics.Bitmap
import android.os.Build


class Util {
    fun getSizeInBytes(bitmap: Bitmap?): Int {
        if (bitmap == null) {
            return 0
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) { //API 19
            try {
                return bitmap.allocationByteCount
            } catch (npe: NullPointerException) {
                // Swallow exception and try fallbacks.
            }
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) { //API 12
            bitmap.byteCount
        } else bitmap.height * bitmap.rowBytes
        //earlier
    }
}