package com.mole.androidcodestudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mole.androidcodestudy.util.currentJob
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
    fun test(){
        viewModelScope.launch(CoroutineName("ViewModel")) {
            //Job<-CoroutineContext.Element<-CoroutineContext Job也是协程上下文
            Job.currentJob()
        }
    }
}