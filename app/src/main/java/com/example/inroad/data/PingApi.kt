package com.example.inroad.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class PingApi {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://f5cc-94-245-181-202.jp.ngrok.io/api/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(PingService::class.java)

    fun getPing() = service.getPing()
}