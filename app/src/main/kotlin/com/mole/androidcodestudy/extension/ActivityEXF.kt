package com.mole.androidcodestudy.extension

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

//如果成功则返回 Intent，如果失败返回null
fun ComponentActivity.registerNormalIntentResultLauncher(callback: ActivityResultCallback<Intent?>): ActivityResultLauncher<Intent> {
    return registerForActivityResult(object : ActivityResultContract<Intent, Intent?>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
            if (resultCode == RESULT_OK) {
                return intent ?: Intent()
            }
            return null
        }

    }, callback)
}

//匹配自定义resultCode,失败返回null给ActivityResultCallback
fun ComponentActivity.registerCustomCodeResultLauncher(
    customResultCode: Int,
    callback: ActivityResultCallback<Intent?>
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(object : ActivityResultContract<Intent, Intent?>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
            if (resultCode == customResultCode) {
                return intent ?: Intent()
            }
            return null
        }

    }, callback)
}

//对ResultApi的封装
fun ComponentActivity.registerResultIntent(
    contract: ActivityResultContract<Intent, Intent?>,
    callback: ActivityResultCallback<Intent?>
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(contract, callback)
}
