package com.mole.androidcodestudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mole.androidcodestudy.coroutine.CustomContinuationInterceptor
import com.mole.androidcodestudy.util.threadLog
import com.mole.androidcodestudy.util.timeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
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
@HiltViewModel
class CoroutineViewModel @Inject constructor() : ViewModel() {
    fun test() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            println(exception.message)
        }
        viewModelScope.launch(CoroutineName("ViewModel") + CustomContinuationInterceptor() + exceptionHandler) {
            //Job<-CoroutineContext.Element<-CoroutineContext Job也是协程上下文
            threadLog("viewModelScope默认调度")
            try {
                coroutineScope {
                    kotlin.runCatching {

                    }.isFailure
                    try {
                        delay(1000)
                        //就算不调用await，async也会在coroutineScope中触发父协程的取消逻辑
                        val job = async {
                            throw (Exception("直接async输出异常"))
                        }
                        job.join()
                        job.await()
                    } catch (e: Exception) {
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
                            throw (Exception("直接async输出异常"))
                        }
                        deferred.await()
                    } catch (e: Exception) {
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
                            throw (Exception("直接async输出异常"))
                        }
                        deferred.join()
                    } catch (e: Exception) {
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
                            throw (Exception("直接launch输出异常"))
                        }
                    } catch (e: Exception) {
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
                            throw (Exception("直接launch输出异常"))
                        }
                        job.join()
                    } catch (e: Exception) {
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
                            throw (Exception("Global async输出异常"))
                        }
                        //没有父协程，不存在取消，所以无法捕获错误
                        deferred.join()
                    } catch (e: Exception) {
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
                            throw (Exception("Global async输出异常"))
                        }
                        //只有await才能捕获异常
                        deferred.await()
                    } catch (e: Exception) {
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
        getUser(object : Callback<String> {
            override fun onSuccess(value: String) {
                continuation.resume(value)
            }

            override fun onError(t: Throwable) {
                continuation.resumeWithException(t)
            }

        })
    }

    suspend fun getUserCoroutineCancelable() = suspendCancellableCoroutine<String> { continuation ->
        continuation.invokeOnCancellation {

        }
        getUser(object : Callback<String> {
            override fun onSuccess(value: String) {
                continuation.resume(value)
            }

            override fun onError(t: Throwable) {
                continuation.resumeWithException(t)
            }

        })
    }


    fun testChannel() {
//        internalChannelTest1()
//        internalChannelTest2()
//        internalChannelTest3()
//        internalChannelTest4()
        internalChannelTest5()
    }

    private fun internalChannelTest1() {
        viewModelScope.launch(Dispatchers.Default) {
            /**
             * RENDEZVOUS：挂起，等待
             * CONFLATED：置换，只保留最后一个send的
             * UNLIMITED：无限制
             * BUFFERED：CHANNEL_DEFAULT_CAPACITY
             */
            val channel = Channel<Int>(capacity = RENDEZVOUS)
            val producer = launch {
                var index = 0
                while (true) {
                    //send也是挂起的，在默认情况下，缓冲为0（RENDEZVOUS：不见不散）时，只有receive了，才能继续send
                    channel.send(index++)
                    delay(100)
                }
            }

            val consumer = launch {
                //在没有值可读时，receive是挂起的
//                val func:suspend ()->Unit = { threadLog(channel.receive().toString()) }
//                while (true){
//                    func()
//                }
                //通过iterator也可以完成对channel的迭代
                for (element in channel) {
                    threadLog(element.toString())
                }
            }
            producer.join()
            consumer.join()
        }
    }

    @ExperimentalCoroutinesApi
    private fun internalChannelTest2() {
        //利用produce启动一个生产者协程，返回ReceiveChannel对象
        //producerScope关闭时，会一同关闭channel
        val producer = viewModelScope.produce(Dispatchers.IO) {
            var index = 0
            while (true) {
                send(index++)
                delay(100)
            }
        }

        viewModelScope.launch {
            while (true) {
                threadLog(producer.receive().toString())
            }
        }
    }

    @ObsoleteCoroutinesApi
    private fun internalChannelTest3() {

        //和produce正好相反，启动消费者协程，并且返回一个SendChannel对象
        //和producerScope相似，actorScope在关闭时，也会关闭channel
        val consumer = viewModelScope.actor {
            while (true) {
                threadLog(receive().toString())
            }
        }

        viewModelScope.launch {
            var index = 0
            while (true) {
                consumer.send(index--)
                delay(100)
            }
        }
    }

    /**
     * 测试channel的关闭
     * 注意要由主导的一方来关闭
     * 例如，actorScope就得注意在合适的时机，触发协程的关闭。
     * 当然，这两个应该不会用，一般都直接channel.close足矣。
     * 打印：
     * [1705484754752] send:0
     * [1705484754752] send:1
     * [1705484754752] send:2
     * [1705484754752] send:3
     * [1705484754753] receive0
     * [1705484754753] send:4
     *
     * [1705484755756] receive1
     *
     * [1705484756759] receive2
     * [1705484757762] receive3
     * [1705484758765] receive4
     */
    private fun internalChannelTest4() {
        viewModelScope.launch {
            val channel = Channel<Int>(3)
            val producer = launch {
                var index = 0
                List(5) {
                    timeLog("send:${index}")
                    channel.send(index++)
                }
                //关闭时，发送端立刻关闭，接收端需要等全部接受后才关闭
                //isClosedForSend立刻为true
                //isClosedForReceive等待接受完为true
                channel.close()
            }

            val consumer = launch {
                for (element in channel) {
                    timeLog("receive${element}")
                    delay(1000)
                }
            }
        }
    }

    /**
     * 测试BroadcastChannel
     * 推荐用SharedFlow，BroadcastChannel以及标记为废弃。
     * 似乎只有独立作用域时，才会按预期生效，还是得注意协程的作用域和取消。
     */
    private fun internalChannelTest5() {
        viewModelScope.launch {
//            val channel = Channel<Int>(5).broadcast()
            val broadcastChannel = BroadcastChannel<Int>(5)

            val producer = GlobalScope.launch {
                List(5) {
                    broadcastChannel.send(it)
                    timeLog("send $it")
                }
                broadcastChannel.close()
            }

            List(3) { index ->
                GlobalScope.launch {
                    val receiveChannel = broadcastChannel.openSubscription()
                    for (element in receiveChannel) {
                        timeLog("[$index] receive: $element")
                        delay(1000)
                    }
                }
            }.forEach { it.join() }

            producer.join()
        }
    }

    /**
     * 测试select，用于在多个await中选择
     * 确认挂起函数是否支持 select，只需要查看其是否存在对应的 SelectClauseN 即可
     * Select 的语义与 Java NIO 或者 Unix 的 IO 多路复用类似，它的存在使得我们可以轻松实现 1 拖 N，实现哪个先来就处理哪个。·
     */
    private fun internalTestSelect() {
        viewModelScope.launch {
            val mock1 = getUserFromApi("aaa")
            val mock2 = getUserFromLocal("aaa")
            //不管哪个先回调，select 立即返回对应回调中的结果
            select {
                mock1.onAwait {

                }
                mock2.onAwait {

                }
            }
        }
    }

    fun CoroutineScope.getUserFromApi(login: String) = async(Dispatchers.IO) {
        delay(1000L * (Math.random().toInt()))
        "mock1"
    }

    fun CoroutineScope.getUserFromLocal(login: String) = async(Dispatchers.IO) {
        delay(1000L * (Math.random().toInt()))
    }

    private fun getUser(callback: Callback<String>) {
//        callback.onSuccess("test getUser")
        callback.onError(Exception("test callback throwable"))
    }

    interface Callback<T> {
        fun onSuccess(value: T)

        fun onError(t: Throwable)
    }
}