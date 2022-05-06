package com.example.inroad.data

import com.example.inroad.data.dto.PointData
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable


class PointRepository {
   // private val apiMock: MockWeatherApi = MockWeatherApi()
    private val api = PointApi()
    private val gson = Gson()

    fun getPoints(): Observable<Array<PointData>> = api.getPoints()

    fun postLocation(latitude: Double, longitude: Double): Completable = api.postLocation(latitude, longitude)
}