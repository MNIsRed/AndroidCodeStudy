package com.mole.androidcodestudy.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.model.ScheduleResult
import com.carterchen247.alarmscheduler.task.AlarmTask
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory
import com.mole.androidcodestudy.CustomApplication
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.ActivityAlarmBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.receiver.ALARM_ACTION
import com.mole.androidcodestudy.receiver.AlarmBroadcastReceiver
import java.time.Duration
import java.util.Date

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/19
 *     desc   : 测试AlarmManager
 *     version: 1.0
 *     参考：
 *     https://developer.android.com/about/versions/14/changes/schedule-exact-alarms?hl=zh-cn
 *     https://carterchen247.medium.com/android-14-behavior-change-schedule-exact-alarms-are-denied-by-default-7563a814dee4
 * </pre>
 */
class AlarmActivity : BaseActivity() {
    private val binding by viewBinding(ActivityAlarmBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btAdd.setOnClickListener {
//            getAlarmScheduler()
            addAlarm()
        }
    }

    private fun addAlarm() {
        val receiverIntent = Intent().apply {
            setClass(this@AlarmActivity, AlarmBroadcastReceiver::class.java)
            action = ALARM_ACTION
            setComponent(ComponentName(this@AlarmActivity.packageName,"${this@AlarmActivity.packageName}.receiver.AlarmBroadcastReceiver"))
        }
        (getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.let { alarmManager ->
            val duration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Duration.ofMinutes(1L).toMillis()
            } else {
                60 * 1000
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    openExactAlarmSettingPage()
                    return
                }
            }
            PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_IMMUTABLE).also {
//                AlarmManagerCompat.setAlarmClock(
//                    alarmManager,
//                    System.currentTimeMillis() + 5 * 1000,
//                    it,
//                    it
//                )
//                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000, it)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000, it)

            }
        }
    }

    private fun getAlarmScheduler() {
        AlarmScheduler.setLogger(AlarmSchedulerLogger.DEBUG)

        AlarmScheduler.setAlarmTaskFactory(object : AlarmTaskFactory {
            override fun createAlarmTask(alarmType: Int): AlarmTask {
                return MyAlarmTask()
            }
        })

        val config = AlarmConfig(
            Date().time + 10000L, // trigger time
            MyAlarmTask.TYPE
        ) {
            dataPayload("reminder" to "have a meeting")
        }

        AlarmScheduler.schedule(config) { result ->
            when (result) {
                is ScheduleResult.Success -> {
                    // alarm scheduling success!
                }

                is ScheduleResult.Failure -> {
                    when (result) {
                        ScheduleResult.Failure.CannotScheduleExactAlarm -> {
                            // handle scenarios like user disables exact alarm permission
                            openExactAlarmSettingPage()
                        }

                        is ScheduleResult.Failure.Error -> {
                            // handle error
                        }
                    }
                }
            }
        }

    }

    fun Activity.openExactAlarmSettingPage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                //跳转app单独设置权限，否则会进入一个app列表
                data = Uri.parse("package:"+this@AlarmActivity.packageName)
            })
        }
    }

    class MyAlarmTask : AlarmTask {
        override fun onAlarmFires(alarmId: Int, dataPayload: DataPayload) {
            // do something with dataPayload you set
            (CustomApplication.getInstance()
                .getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let { notificationManager ->
                NotificationCompat.Builder(CustomApplication.getInstance()).apply {
                    setContentTitle("测试通知标题")
                    setContentText("测试通知内容")
                    //默认优先级
                    setPriority(NotificationCompat.PRIORITY_MAX)
                    //提醒方式 声音，闪光等
                    setDefaults(NotificationCompat.DEFAULT_ALL)
                    setSmallIcon(R.mipmap.ic_launcher)
                    setAutoCancel(true)
                    //必须设置ChannelId
                    setChannelId("1")
                    PendingIntent.getActivity(
                        CustomApplication.getInstance(),
                        0,
                        Intent(CustomApplication.getInstance(), MainActivity::class.java),
                        PendingIntent.FLAG_IMMUTABLE
                    ).also {
                        setContentIntent(it)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 8.0以上系统，通知需要加channel信息
                        val channel = NotificationChannel(
                            "1", "channel",
                            NotificationManager.IMPORTANCE_HIGH
                        )
                        notificationManager.createNotificationChannel(channel)
                    }
                    //Android13 需要权限POST_NOTIFICATIONS
                    notificationManager.notify(0, build())
                }
            }
        }

        companion object {
            const val TYPE = 1
        }
    }
}