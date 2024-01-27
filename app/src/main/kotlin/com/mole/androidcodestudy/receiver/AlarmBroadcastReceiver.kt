package com.mole.androidcodestudy.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.activity.MainActivity


/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/24
 *     desc   : 接受定时器广播
 *     version: 1.0
 * </pre>
 */
const val ALARM_ACTION = "com.mole.androidcodestudy.alarm_action"

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmBroadcastReceiver","AlarmBroadcastReceiver触发")
        context?.let { context ->
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let { notificationManager ->
                NotificationCompat.Builder(context).apply {
                    setContentTitle("测试通知标题")
                    setContentText("测试通知内容")
                    //默认优先级
                    setPriority(PRIORITY_DEFAULT)
                    //提醒方式 声音，闪光等
                    setDefaults(DEFAULT_ALL)
                    setSmallIcon(R.mipmap.ic_launcher)
                    setAutoCancel(true)
                    setChannelId("2333")
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, MainActivity::class.java),
                        PendingIntent.FLAG_IMMUTABLE
                    ).also {
                        setContentIntent(it)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // 8.0以上系统，通知需要加channel信息
                        val channel = NotificationChannel(
                            "2333", "channel",
                            NotificationManager.IMPORTANCE_HIGH
                        )
                        notificationManager.createNotificationChannel(channel)
                    }
                    //Android13 需要权限POST_NOTIFICATIONS
                    notificationManager.notify(0, build())
                }
            }
        }
    }
}