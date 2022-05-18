package com.example.inroad.data

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface PingService {

    @GET("Ping/GetPing")
    fun getPing(): Observable<Void>
}