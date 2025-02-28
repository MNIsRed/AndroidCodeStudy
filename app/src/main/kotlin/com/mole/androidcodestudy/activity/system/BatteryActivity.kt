package com.mole.androidcodestudy.activity.system

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.ActivityBatteryBinding
import com.mole.androidcodestudy.extension.viewBinding

class BatteryActivity : AppCompatActivity() {
    private val binding: ActivityBatteryBinding by viewBinding()
    private lateinit var batteryReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery)
        var hasIgnored = false
        (getSystemService(POWER_SERVICE) as? PowerManager)?.let { power ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hasIgnored = power.isIgnoringBatteryOptimizations(packageName)
            }
        }

        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    val batteryPct = level * 100 / scale.toFloat()

                    val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
                            || status == BatteryManager.BATTERY_STATUS_FULL

                    binding.tvBatteryInfo.text = """
                        电池电量：${batteryPct}%
                        充电状态：${if (isCharging) "正在充电" else "未充电"}
                        已加入电池优化忽略：${hasIgnored}
                    """.trimIndent()
                }
            }
        }


        binding.btnBatterySettings.setOnClickListener {
            val intent = Intent()
            //需要Manifest 声明REQUEST_IGNORE_BATTERY_OPTIMIZATIONS权限
            //intent.action = android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            //intent.setData(Uri.parse("package:${packageName}"))

            //拉起智能管理页面
            //intent.action = android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS

            //和 2 一样
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //
            //intent.addCategory(Intent.CATEGORY_LAUNCHER)
            //
            //val cn = ComponentName.unflattenFromString("com.android.settings/.Settings\$HighPowerApplicationsActivity")
            //
            //intent.setComponent(cn)

            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryReceiver)
    }
} 