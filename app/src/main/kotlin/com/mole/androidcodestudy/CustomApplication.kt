package com.mole.androidcodestudy

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import android.util.Log
import coil.Coil
import coil.ImageLoader
// YCAndroidTool disabled: missing artifacts in repos.
import java.io.File
import xcrash.ICrashCallback
import xcrash.XCrash
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initXCrash()
        // initYCAndroidTool() // disabled: missing artifacts in repos.
        System.loadLibrary("sqlcipher")
        
        // 初始化Coil
        val imageLoader = ImageLoader.Builder(this)
            .crossfade(true)
            .build()
        Coil.setImageLoader(imageLoader)
    }

    /**
     * 如果在onCreate中初始化，调用此方法
     * 也可以直接重写[getWorkManagerConfiguration]
     */
    private fun initInCreate() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setJobSchedulerJobIdRange(1000, 2000) // Set the job ID range here
            .build()

        WorkManager.initialize(this, config)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setJobSchedulerJobIdRange(1000, 2000) // Set the job ID range here
            .build()


    /**
     * 初始化 xCrash 用于捕获 native/ANR 崩溃
     */
    private fun initXCrash() {
        val logDir = File(filesDir, "xcrash").apply { mkdirs() }.absolutePath
        val params = XCrash.InitParameters()
            .setLogDir(logDir)
            .setAppVersion("1.0.1")
            .setJavaRethrow(true) // 继续交给系统默认处理
            .setAnrRethrow(true)
            .setNativeRethrow(false)
            .setNativeDumpAllThreads(true)
            .setNativeDumpMap(true)
            .setNativeDumpFds(true)
            .setNativeLogCountMax(5)
            .setJavaCallback(ICrashCallback { logPath, emergency ->
                Log.e("XCrash", "Java 崩溃日志: $logPath emergency: $emergency")
            })
            .setAnrCallback(ICrashCallback { logPath, emergency ->
                Log.e("XCrash", "ANR 日志: $logPath emergency: $emergency")
            })
            .setNativeCallback(ICrashCallback { logPath, emergency ->
                Log.e("XCrash", "native 崩溃捕获，日志路径: $logPath, emergency: $emergency")
            })
        XCrash.init(this, params)
    }

    companion object {
        private lateinit var INSTANCE: CustomApplication
        fun getInstance(): CustomApplication {
            return INSTANCE
        }
    }
}
