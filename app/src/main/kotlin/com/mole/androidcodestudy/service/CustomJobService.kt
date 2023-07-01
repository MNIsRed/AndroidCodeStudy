package com.mole.androidcodestudy.service

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
@SuppressLint("SpecifyJobSchedulerIdRange")
class CustomJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

}
