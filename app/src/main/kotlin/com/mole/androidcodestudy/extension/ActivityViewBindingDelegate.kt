package com.mole.androidcodestudy.extension

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/10/25
 *     desc   :
 *     ActivityViewBindingDelegate反射bind方法
 *     ActivityInflateViewBindingDelegate反射inflate方法
 *     ActivityViewBindingDelegateNoReflect利用kotlin的函数参数类型特性，摆脱了反射。
 *     单论写法上来说，inflate明显更效率，因为不需要在Activity处先膨胀布局
 *     无发射的方式虽然使用上多写一点点，但是感觉上是最合适的。
 *     version: 1.0
 * </pre>
 */
class ActivityViewBindingDelegate<VB:ViewBinding>(clazz: Class<VB>) : ReadOnlyProperty<Activity,VB>{

    private val bindingMethod = clazz.getMethod("bind",View::class.java)
    override fun getValue(thisRef: Activity, property: KProperty<*>): VB {
        val view = thisRef.findViewById<ViewGroup>(android.R.id.content)
            ?.children
            ?.first()
        return bindingMethod.invoke(null,view) as VB
    }
}

class ActivityInflateViewBindingDelegate<VB:ViewBinding>(clazz: Class<VB>) : ReadOnlyProperty<Activity,VB>{

    private val bindingMethod = clazz.getMethod("inflate", LayoutInflater::class.java)
    override fun getValue(thisRef: Activity, property: KProperty<*>): VB {

        return (bindingMethod.invoke(null,thisRef.layoutInflater) as VB).also {
            thisRef.setContentView(it.root)
        }
    }
}

class ActivityViewBindingDelegateNoReflect<VB:ViewBinding>(private val inflateFunc:(LayoutInflater)->VB) : ReadOnlyProperty<Activity,VB>{

    override fun getValue(thisRef: Activity, property: KProperty<*>): VB {
        return inflateFunc(thisRef.layoutInflater).also {
            thisRef.setContentView(it.root)
        }
    }
}


inline fun <reified VB:ViewBinding>AppCompatActivity.viewBinding() = ActivityViewBindingDelegate(VB::class.java)
inline fun <reified VB:ViewBinding>AppCompatActivity.viewBindingByInflate() = ActivityInflateViewBindingDelegate(VB::class.java)
fun <VB:ViewBinding>AppCompatActivity.viewBinding(inflateFunc:(LayoutInflater)->VB) = ActivityViewBindingDelegateNoReflect(inflateFunc)