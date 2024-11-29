package com.mole.androidcodestudy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/11/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ParentFragmentViewModel : ViewModel() {
    private val _text: MutableLiveData<String?> = MutableLiveData()
    val text: LiveData<String?> = _text

    fun changeText(newText: String) {
        _text.value = newText
    }
}