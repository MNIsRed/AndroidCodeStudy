package com.mole.net

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val LOCAL_NET_ADDRESS = "192.168.50.146"

private val service by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.50.146:4523/m1/1022018-0-default/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(Api::class.java)
}

@Module
@InstallIn(ViewModelComponent::class)
object ApiModule{
    @Provides
    fun apiProvider():Api{
        return Retrofit.Builder()
            .baseUrl("http://192.168.50.146:4523/m1/1022018-0-default/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Api::class.java)
    }
}

fun getNetworkService() = service