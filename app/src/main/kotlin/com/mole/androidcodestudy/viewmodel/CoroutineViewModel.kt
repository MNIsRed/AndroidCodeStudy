package com.mole.androidcodestudy.viewmodel

import android.provider.Settings.Global
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mole.androidcodestudy.coroutine.CustomContinuationInterceptor
import com.mole.androidcodestudy.util.currentJob
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CoroutineViewModel : ViewModel() {
    fun test() {
        val exceptionHandler = CoroutineExceptionHandler{coroutineContext,exception->
            println(exception.message)
        }
        viewModelScope.launch(CoroutineName("ViewModel") + CustomContinuationInterceptor()+exceptionHandler) {
            //Job<-CoroutineContext.Element<-CoroutineContext Job也是协程上下文
            Job.currentJob()

            try {
                coroutineScope {
                    try {
                        //就算不调用await，async也会在coroutineScope中触发父协程的取消逻辑
                        async {
                            throw(Exception("直接async输出异常"))
                        }
                    }catch (e:Exception){
                        println("父协程内捕获：${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("捕获到了子协程异常1：${e.message}")
            }

            try {
                coroutineScope {
                    try {
                        val deferred = async {
                            throw(Exception("直接async输出异常"))
                        }
                        deferred.await()
                    }catch (e:Exception){
                        println("父协程内捕获：${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("捕获到了子协程异常2：${e.message}")
            }

            try {
                coroutineScope {
                    try {
                        val deferred = async {
                            throw(Exception("直接async输出异常"))
                        }
                        deferred.join()
                    }catch (e:Exception){
                        println("父协程内捕获：${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("捕获到了子协程异常3：${e.message}")
            }

            try {
                coroutineScope {
                    try {
                         launch {
                            throw(Exception("直接launch输出异常"))
                        }
                    }catch (e:Exception){
                        println("父协程内捕获：${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("捕获到了子协程异常4：${e.message}")
            }
            try {
                coroutineScope {
                    try {
                        val job = launch {
                            throw(Exception("直接launch输出异常"))
                        }
                        job.join()
                    }catch (e:Exception){
                        println("父协程内捕获：${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("捕获到了子协程异常5：${e.message}")
            }
            try {
                coroutineScope {
                    try {
                        //因为是通过GlobalScope，所以没有父协程，所以coroutineScope也无法捕获其错误
                        val deferred = GlobalScope.async {
                            throw(Exception("Global async输出异常"))
                        }
                        //没有父协程，不存在取消，所以无法捕获错误
                        deferred.join()
                    }catch (e:Exception){
                        println("父协程内捕获：${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("捕获到了子协程异常6：${e.message}")
            }
            try {
                coroutineScope {
                    try {
                        //因为是通过GlobalScope，所以没有父协程，所以coroutineScope也无法捕获其错误
                        val deferred = GlobalScope.async {
                            throw(Exception("Global async输出异常"))
                        }
                        //只有await才能捕获异常
                        deferred.await()
                    }catch (e:Exception){
                        println("父协程内捕获：${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("捕获到了子协程异常7：${e.message}")
            }
        }
    }

    //suspendCoroutine将挂起函数与回调风格的异步API结合起来
    suspend fun getUserCoroutine() = suspendCoroutine<String> { continuation ->
        getUser (object : Callback<String>{
            override fun onSuccess(value: String) {
                continuation.resume(value)
            }

            override fun onError(t: Throwable) {
                continuation.resumeWithException(t)
            }

        })
    }

    suspend fun getUserCoroutineCancelable() = suspendCancellableCoroutine<String> { continuation ->
        getUser (object : Callback<String>{
            override fun onSuccess(value: String) {
                continuation.resume(value)
            }

            override fun onError(t: Throwable) {
                continuation.resumeWithException(t)
            }

        })
    }


    fun getUser(callback: Callback<String>) {
//        callback.onSuccess("test getUser")
        callback.onError(Exception("test callback throwable"))
    }

    interface Callback<T> {
        fun onSuccess(value: T)

        fun onError(t: Throwable)
    }
}