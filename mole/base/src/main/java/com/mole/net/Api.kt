package com.mole.net

import com.mole.net.response.PetResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("pet/{petId}")
    suspend fun getPet(@Path("petId") petId: String): PetResponse
}