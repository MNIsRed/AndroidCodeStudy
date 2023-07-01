package com.mole.androidcodestudy

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
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
}