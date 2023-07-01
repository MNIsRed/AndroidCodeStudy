package com.mole.androidcodestudy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mole.net.Api
import com.mole.net.response.PetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel中使用Hilt的方式
 * 头部@HiltViewModel注解+@Inject注解需要注入的对象，对象需要放在默认构造函数中
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val api: Api) : ViewModel() {
    private val _pet = MutableLiveData<PetResponse>()
    val pet : LiveData<PetResponse> = _pet
    fun updatePet(){
        viewModelScope.launch {
            try {
                val result = api.getPet("1")
                _pet.value = result
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}