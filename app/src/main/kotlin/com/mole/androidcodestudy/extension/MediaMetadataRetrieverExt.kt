package com.mole.androidcodestudy.extension

import android.media.MediaMetadataRetriever

/**
 * 兼容 API 21+ 的 MediaMetadataRetriever 自动释放扩展。
 * 在 API 29 之前没有实现 AutoCloseable，不能直接用 use。
 */
inline fun <T> MediaMetadataRetriever.useCompat(block: (MediaMetadataRetriever) -> T): T {
    try {
        return block(this)
    } finally {
        release()
    }
}
