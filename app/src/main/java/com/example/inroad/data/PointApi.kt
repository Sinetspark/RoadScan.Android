package com.example.inroad.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PointApi {
    //TO DO
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(PointService::class.java)

    fun getPoints() = service.getPoints()

    fun postLocation(latitude: Double, longitude: Double) =
        service.postLocatiton("$latitude, $longitude")
}