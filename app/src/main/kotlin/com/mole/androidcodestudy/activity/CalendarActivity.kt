package com.mole.androidcodestudy.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.mole.androidcodestudy.databinding.ActivityCalendarBinding
import com.mole.androidcodestudy.extension.hasM
import com.mole.androidcodestudy.extension.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/01/18
 *     desc   : 测试添加日历事件
 *     version: 1.0
 * </pre>
 */
class CalendarActivity : BaseActivity() {
    private val binding by viewBinding(ActivityCalendarBinding::inflate)


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.buttonAdd.setOnClickListener {
            val hasPermission =
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_CALENDAR
                ) && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CALENDAR
                )
            if (hasM && !hasPermission) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                    ), 0
                )
                return@setOnClickListener
            }
            addCalendarEvent()
        }
        binding.buttonDelete.setOnClickListener {
            deleteCalendar()
        }
    }

    private fun addCalendarEvent() {
        lifecycleScope.launch(Dispatchers.IO) {
            val calID: Long = getCalendarId()
            val startMillis: Long = Calendar.getInstance().run {
                set(2024, 0, 18, 18, 15, 0)
                timeInMillis
            }
            val endMillis: Long = Calendar.getInstance().run {
                set(2024, 0, 18, 19, 30)
                timeInMillis
            }

            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(
                    CalendarContract.Events.TITLE,
                    binding.etTitle.text?.toString() ?: "This is title"
                )
                put(CalendarContract.Events.CALENDAR_ID, calID)
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().displayName)
                put(CalendarContract.Events.DESCRIPTION, "事件描述")
            }
            val uri: Uri? = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

            //事件提醒的设定
            ContentValues().apply {
                // get the event ID that is the last element in the Uri
                put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(uri!!))
                put(CalendarContract.Reminders.MINUTES, 15) // 提前previousDate天有提醒
                put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
                try {
                    contentResolver
                        .insert(CalendarContract.Reminders.CONTENT_URI, this)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun deleteCalendar() {
        lifecycleScope.launch(Dispatchers.IO) {
            val syncUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(
                    CalendarContract.Calendars.ACCOUNT_NAME,
                    "AndroidCodeStudy"
                )
                .appendQueryParameter(
                    CalendarContract.Calendars.ACCOUNT_TYPE,
                    CalendarContract.ACCOUNT_TYPE_LOCAL
                )
                .build()

            contentResolver.delete(syncUri, null, null)
        }
    }

    //获取日历账户id
    private fun getCalendarId(): Long {
        // Run query
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        var calendarId: Long = -1
        val cur: Cursor =
            contentResolver.query(uri, null, null, null, null)
                ?: return calendarId

        // Use the cursor to step through the returned records
        val idIndex = cur.getColumnIndexOrThrow(CalendarContract.Calendars._ID)
        val nameIndex = cur.getColumnIndexOrThrow(CalendarContract.Calendars.NAME)
        while (cur.moveToNext()) {
            if (cur.getString(nameIndex) == CALENDAR_NAME) {
                calendarId = cur.getLong(idIndex)
                break
            }
        }
        cur.close()

        if (calendarId != -1L) {
            return calendarId
        }

        return addCalendarAccount()
    }

    private fun addCalendarAccount(): Long {
        val timeZone = TimeZone.getDefault()
        var calendarAccountId = -1L
        ContentValues().apply {
            //必须项

            //日历相关联的账户名称
            put(
                CalendarContract.Calendars.ACCOUNT_NAME,
                CALENDAR_NAME
            )
            //日历相关联的账户类型
            put(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL
            )
            //日历的名称
            put(CalendarContract.Calendars.NAME, CALENDAR_NAME)
            //日历在设备上显示的名称
            put(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                "测试日历"
            )
            //日历的颜色
            put(CalendarContract.Calendars.CALENDAR_COLOR, Color.GREEN)
            //日历的访问级别为所有者级别
            put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER
            )
            //指定日历的所有者账户 设置为""，将无法被显示
            put(
                CalendarContract.Calendars.OWNER_ACCOUNT,
                "company@mail"
            )
        }.apply {
            //可选项

            //日历在设备上可见，值为 1 表示可见
            put(CalendarContract.Calendars.VISIBLE, 1)
            // 启用日历事件同步，值为 1 表示启用
            put(CalendarContract.Calendars.SYNC_EVENTS, 1)
            //设置日历的时区
            put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.id)
            //设置是否允许组织者响应事件，值为 0 表示不允许
            put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
        }.also {
            // 同步适配器执行操作，必须设置这些
            // https://developer.android.com/reference/android/provider/CalendarContract.Calendars#operations
            val syncUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_NAME)
                .appendQueryParameter(
                    CalendarContract.Calendars.ACCOUNT_TYPE,
                    CalendarContract.ACCOUNT_TYPE_LOCAL
                )
                .build()

            contentResolver.insert(syncUri, it)
                ?.let { calendarUri ->
                    calendarAccountId = ContentUris.parseId(calendarUri)
                }
        }
        return calendarAccountId
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        addCalendarEvent()
    }

    companion object {
        private const val CALENDAR_NAME: String = "AndroidCodeStudy"
    }
}