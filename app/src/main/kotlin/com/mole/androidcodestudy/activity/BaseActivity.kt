package com.mole.androidcodestudy.activity

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * 所有Activity的基类
 * 当前功能
 * 1.打印生命周期日志
 */
private const val TAG = "BaseActivity"
open class BaseActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"${this.javaClass.simpleName}.onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG,"${this.javaClass.simpleName}.onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"${this.javaClass.simpleName}.onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,"${this.javaClass.simpleName}.onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"${this.javaClass.simpleName}.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"${this.javaClass.simpleName}.onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG,"${this.javaClass.simpleName}.onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG,"${this.javaClass.simpleName}.onRestoreInstanceState")
    }

    /**
     * 如果屏幕旋转时不重建，想触发onConfigurationChanged。
     * 需要设置configChanges为：“orientation|screenSize”
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(TAG,"${this.javaClass.simpleName}.onConfigurationChanged")
    }
}