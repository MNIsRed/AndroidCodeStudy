package com.mole.androidcodestudy.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.mole.androidcodestudy.databinding.ActivityConfigurationChangeTestBinding

/**
 * 功能：
 * 1.测试configurationChanged的触发条件
 */
class ConfigurationChangeTestActivity : BaseActivity(){
    private lateinit var binding : ActivityConfigurationChangeTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
//        setActivityOrientation()
        super.onCreate(savedInstanceState)
        binding = ActivityConfigurationChangeTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * 通过代码指定Activity只支持某个方向，比如竖着or横着
     * 也可以在AndroidManifest里通过android:screenOrientation属性指定
     *
     * Tip:
     * 1.如果限制了屏幕方向，可能会影响onConfigurationChanged方法的触发.
     * 当使用AndroidManifest里声明screenOrientation，系统不会触发配置变化。
     * 但是如果通过代码设置requestedOrientation，不论时机，都会触发onConfigurationChanged。
     */
    fun setActivityOrientation(){
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ConfigurationChangeTestActivity::class.java)
            context.startActivity(starter)
        }
    }
}