package com.mole.androidcodestudy.activity.library

import android.os.Bundle
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.data.AutoValueTestBean
import com.mole.androidcodestudy.data.KotlinDataClassBean
import com.mole.androidcodestudy.data.LombokKotlinBean
import com.mole.androidcodestudy.data.LombokTestBean
import com.mole.androidcodestudy.databinding.ActivityLombokBinding
import com.mole.androidcodestudy.extension.viewBindingByInflate

class LombokActivity : BaseActivity() {
    private val binding by viewBindingByInflate<ActivityLombokBinding>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bean = LombokTestBean("testString",0)
        val bean2 = LombokKotlinBean("testKotlinLombok",1)
        //dataclass会生成toString方法，展示所有属性
        val bean3 = KotlinDataClassBean("kotlinDataClass",2)
        val bean4 = AutoValueTestBean.create("autoValue",3)
        binding.tvProperty.text = buildString {
            showToString("@ToString注解的Java：",bean)
            showToString("@ToString注解的Kotlin：",bean2)
            showToString("Kotlin的DataClass：",bean3)
            showToString("AutoValue注解Java类：",bean4)
        }
    }


    private fun StringBuilder.showToString(des:String,bean:Any){
        append(des)
        append(bean.toString())
        appendLine()
    }
}