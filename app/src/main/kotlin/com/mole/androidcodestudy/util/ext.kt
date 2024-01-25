package com.mole.androidcodestudy.util

import android.os.Build
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.mole.androidcodestudy.CustomApplication
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.newCoroutineContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@ColorInt fun Int.getColor():Int{
    return ContextCompat.getColor(CustomApplication.getInstance(),this)
}

/**
 * 获取当前Job
 * CoroutineContext中应该是有一个coroutineContext的引用，用于获取当前协程上下文？
 * 因为Key是一个伴生对象，所以意味着，同一个coroutineContext中，只存在一个Job，同理ContinuationInterceptor也是只存在一个
 */
suspend fun Job.Key.currentJob() = coroutineContext[Job]

fun threadLog(msg:String) = println("[${Thread.currentThread().name}] $msg")
fun timeLog(msg:String) = println("[${System.currentTimeMillis()}] $msg")

