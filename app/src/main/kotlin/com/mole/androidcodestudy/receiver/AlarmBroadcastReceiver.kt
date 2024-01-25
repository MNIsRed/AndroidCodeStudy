package com.mole.androidcodestudy.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.activity.MainActivity
import okhttp3.internal.notify


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
        context?.let {context->
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let {notificationManager->
                NotificationCompat.Builder(context).apply {
                    setContentTitle("测试通知标题")
                    setContentText("测试通知内容")
                    //默认优先级
                    setPriority(PRIORITY_DEFAULT)
                    //提醒方式 声音，闪光等
                    setDefaults(DEFAULT_ALL)
                    setSmallIcon(R.mipmap.ic_launcher)
                    setAutoCancel(true)

                    PendingIntent.getActivity(context,0,Intent(context,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE).also {
                        setContentIntent(it)
                    }
                    //Android13 需要权限POST_NOTIFICATIONS
                    notificationManager.notify(0,build())
                }
            }
        }
    }
}