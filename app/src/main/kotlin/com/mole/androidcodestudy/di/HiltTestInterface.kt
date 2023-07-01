package com.mole.androidcodestudy.di

import android.util.Log
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

interface HiltTestInterface {
    fun print()
}

class HiltTestInterfaceImpl @Inject constructor(): HiltTestInterface {
    override fun print() {
        println()
        Log.d("hilt test","log output")
    }

}

@Module
@InstallIn(ActivityComponent::class)
abstract class HiltTestModule(){
    @Binds
    abstract fun bindHiltTestInterface(hiltTestInterfaceImpl: HiltTestInterfaceImpl): HiltTestInterface
}