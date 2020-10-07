package com.example.passwordmanager.helpers

import android.content.Context
import com.example.passwordmanager.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//object SwitchServiceProvider {
//
//    fun getService(): SwitchService = retrofit.create(SwitchService::class.java)
//
//    private val retrofit =
//        Retrofit
//            .Builder()
//            .baseUrl("http://121.130.15.124:9090")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//
//}

class SwitchServiceProvider {
    companion object {
        fun getProvider(context: Context): SwitchService {
            val retrofit: Retrofit = Retrofit
                .Builder()
                .baseUrl(context.resources.getString(R.string.switch_api_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(SwitchService::class.java)
        }
    }
}