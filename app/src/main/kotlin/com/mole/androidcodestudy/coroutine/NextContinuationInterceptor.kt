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
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class NextContinuationInterceptor : ContinuationInterceptor{
    override val key: CoroutineContext.Key<*> = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        Log.d("Interceptor","NextContinuationInterceptor触发拦截")
        return continuation
    }
}