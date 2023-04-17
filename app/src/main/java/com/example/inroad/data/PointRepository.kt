package com.example.inroad.data

import com.example.inroad.data.dto.PointData
import com.example.inroad.data.dto.PointStatus
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable


class PointRepository {
    private val api = PointApi()

    fun getPoints(latitude: Double, longitude: Double, minDistance: Int, maxDistance: Int): Observable<Array<PointData>> =
        api.getPoints(latitude, longitude, minDistance, maxDistance)
}