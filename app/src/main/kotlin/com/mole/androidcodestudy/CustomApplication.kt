package com.mole.androidcodestudy

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import android.util.Log
import coil.Coil
import coil.ImageLoader
import com.yc.toollib.crash.CrashHandler
import com.yc.toollib.crash.CrashListener
import com.yc.toollib.crash.CrashToolUtils
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
        initYCAndroidTool()
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

    private fun initYCAndroidTool() {
        CrashHandler.getInstance().init(this, object : CrashListener {
            /**
             * 重启app
             */
            override fun againStartApp() {
                CrashToolUtils.reStartApp1(this@CustomApplication, 1000)
                //CrashToolUtils.reStartApp2(App.this,1000, MainActivity.class);
                //CrashToolUtils.reStartApp3(AppManager.getAppManager().currentActivity());
            }

            /**
             * 自定义上传crash，支持开发者上传自己捕获的crash数据
             * @param ex                        ex
             */
            override fun recordException(ex: Throwable?) {
                //自定义上传crash，支持开发者上传自己捕获的crash数据
                //StatService.recordException(getApplication(), ex);
                //崩溃文件存储路径：/storage/emulated/0/Android/data/你的包名/cache/crashLogs
            }
        })
    }

    /**
     * 初始化 xCrash 用于捕获 native/ANR 崩溃，并在 Java 崩溃后继续让 CrashHandler 处理
     */
    private fun initXCrash() {
        val logDir = File(filesDir, "xcrash").apply { mkdirs() }.absolutePath
        val params = XCrash.InitParameters()
            .setLogDir(logDir)
            .setAppVersion("1.0.1")
            .setJavaRethrow(true) // 处理完 Java 崩溃后继续交给现有 CrashHandler
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
