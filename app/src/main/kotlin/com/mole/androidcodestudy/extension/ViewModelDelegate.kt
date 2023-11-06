package com.mole.androidcodestudy.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/11/02
 *     desc   : reified实现编译期获取泛型类型，完成viewModel绑定，思路参照ActivityViewBindingDelegate
 *     实际不需要，activity-ktx提供了对应的扩展函数viewModels()
 *     version: 1.0
 * </pre>
 */
class ViewModelDelegate<VM:ViewModel>(private val clazz:Class<VM>) : ReadOnlyProperty<AppCompatActivity,VM>{
    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): VM {
        return ViewModelProvider(thisRef)[clazz]
    }
}

inline fun<reified VM:ViewModel> AppCompatActivity.viewModelProvider() = ViewModelDelegate(VM::class.java)