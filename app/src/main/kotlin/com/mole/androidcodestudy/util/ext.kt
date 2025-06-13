package com.mole.androidcodestudy.util

import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.mole.androidcodestudy.CustomApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

@ColorInt
fun Int.getColor(): Int {
    return ContextCompat.getColor(CustomApplication.getInstance(), this)
}

/**
 * 获取当前Job
 * CoroutineContext中应该是有一个coroutineContext的引用，用于获取当前协程上下文？
 * 因为Key是一个伴生对象，所以意味着，同一个coroutineContext中，只存在一个Job，同理ContinuationInterceptor也是只存在一个
 */
suspend fun Job.Key.currentJob() = coroutineContext[Job]

fun threadLog(msg: String) = println("[${Thread.currentThread().name}] $msg")
fun timeLog(msg: String) = println("[${System.currentTimeMillis()}] $msg")


fun <T> CoroutineScope.autoTryCatchAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
) = this.async(context, start) {
    try {
        block()
    } catch (e: Exception) {
        println("Exception caught in async lambda: ${e.message}")
    }
}

suspend fun <T> Deferred<T>.autoTryCatchAwait(): T {
    try {
        return this.await()
    } catch (e: Exception) {
        println("Exception caught in await: ${e.message}")
        throw e // 重新抛出异常，或者返回一个默认值
    }
}


