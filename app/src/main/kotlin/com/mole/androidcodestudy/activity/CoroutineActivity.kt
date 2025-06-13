package com.mole.androidcodestudy.activity

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.mole.androidcodestudy.databinding.ActivityCoroutineBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.extension.viewModelProvider
import com.mole.androidcodestudy.util.threadLog
import com.mole.androidcodestudy.viewmodel.CoroutineViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/11
 *     desc   : 协程相关特性
 *     version: 1.0
 * </pre>
 */
class CoroutineActivity : BaseActivity() {
    private val binding by viewBinding(ActivityCoroutineBinding::inflate)
    private val viewModel: CoroutineViewModel by viewModelProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.button.setOnClickListener {
            viewModel.test()
            lifecycleScope.launch {
                threadLog("默认调度1")
                launch(Dispatchers.Main) {
                    threadLog("调度Main")
                }
                launch(Dispatchers.IO) {
                    threadLog("调度IO")
                }
                launch(Dispatchers.Default) {
                    threadLog("调度Default")
                }
            }
            viewModel.testChannel()
        }

        binding.buttonFlow.setOnClickListener {
            lifecycleScope.launch {
                var countDownTimer = 1

                val job = lifecycleScope.launch(Dispatchers.Default) {
                    flow<Int> {
                        while (countDownTimer < 100) {
                            emit(countDownTimer)
                            delay(1000)
                            countDownTimer++
                        }
                    }.onEach {
                        lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(
                                this@CoroutineActivity,
                                "触发第${countDownTimer}次",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.flowOn(Dispatchers.Main).collect {}
                }

                delay(5000)
                job.cancel()
            }
        }
    }
}