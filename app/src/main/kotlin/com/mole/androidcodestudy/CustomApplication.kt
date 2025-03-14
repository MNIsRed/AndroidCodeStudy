package com.mole.androidcodestudy

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.yc.toollib.crash.CrashHandler
import com.yc.toollib.crash.CrashListener
import com.yc.toollib.crash.CrashToolUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initYCAndroidTool()
        System.loadLibrary("sqlcipher")
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

    companion object {
        private lateinit var INSTANCE: CustomApplication
        fun getInstance(): CustomApplication {
            return INSTANCE
        }
    }
}