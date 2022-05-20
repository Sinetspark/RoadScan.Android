package com.example.inroad.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class PingApi {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://49d1-80-73-72-217.eu.ngrok.io/api/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(PingService::class.java)

    fun getPing() = service.getPing()
}