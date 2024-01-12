package com.mole.androidcodestudy.coroutine

import android.util.Log
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/12
 *     desc   : 自定义一个拦截器，尝试能否实现协程拦截器的简单的责任链
 *     version: 1.0
 * </pre>
 */
class CustomContinuationInterceptor : ContinuationInterceptor{
    override val key: CoroutineContext.Key<*> = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        Log.d("Interceptor","CustomContinuationInterceptor触发拦截")
        return NextContinuationInterceptor().interceptContinuation(continuation)
    }
}