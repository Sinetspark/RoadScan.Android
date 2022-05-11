package com.example.inroad.data

import com.example.inroad.data.dto.BumpData
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class BumpRepository {
    private val api = BumpApi()
    private val gson = Gson()

    fun getPoints(): Observable<Array<BumpData>> = api.getBumps()

    fun postLocation(latitude: Double, longitude: Double): Completable = api.postLocation(latitude, longitude)
}