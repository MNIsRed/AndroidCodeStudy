package com.mole.androidcodestudy.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.mole.androidcodestudy.databinding.ActivityAlarmBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.receiver.ALARM_ACTION
import com.mole.androidcodestudy.receiver.AlarmBroadcastReceiver
import java.time.Duration

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/19
 *     desc   : 测试AlarmManager
 *     version: 1.0
 *     参考：
 *     https://developer.android.com/about/versions/14/changes/schedule-exact-alarms?hl=zh-cn
 *     https://developer.android.com/about/versions/14/changes/schedule-exact-alarms?hl=zh-cn
 * </pre>
 */
class AlarmActivity : BaseActivity() {
    private val binding by viewBinding(ActivityAlarmBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btAdd.setOnClickListener {
            addAlarm()
        }
    }

    private fun addAlarm() {
        val receiverIntent = Intent().apply {
            setClass(this@AlarmActivity, AlarmBroadcastReceiver::class.java)
            action = ALARM_ACTION
        }
        (getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.let { alarmManager ->
            val duration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Duration.ofMinutes(1L).toMillis()
            } else {
                60 * 1000
            }
            PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_IMMUTABLE).also {
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000, it)
            }
        }
    }
}