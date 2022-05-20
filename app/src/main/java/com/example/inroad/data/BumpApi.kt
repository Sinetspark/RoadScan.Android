package com.example.inroad.data

import com.example.inroad.data.dto.GetPoints
import com.example.inroad.data.dto.InsertBumps
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class BumpApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://49d1-80-73-72-217.eu.ngrok.io/api/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(BumpService::class.java)

    fun insertBumps(insertBumps: InsertBumps) = service.insertBumps(insertBumps)
}