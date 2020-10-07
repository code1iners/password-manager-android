package com.example.passwordmanager.helpers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SwitchService {
    @GET("api/v1/apps/list/")
    fun appsListRead(): Call<JsonArray>

    @POST("api/v1/apps/list/")
    fun appsListPost(@Body jsonObject: JsonObject): Call<JsonObject>
}