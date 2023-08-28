package com.mole.androidcodestudy.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Observable

/**
 * 在这个页面测试RxJava框架
 */
class RxJavaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun plainObserve(){
        Observable.just("hello","world").subscribe{
            Log.d("RxJavaStudy",it)
        }
    }
}