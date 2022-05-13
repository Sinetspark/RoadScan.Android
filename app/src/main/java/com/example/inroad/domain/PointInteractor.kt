package com.example.inroad.domain

import android.content.Context
import com.example.inroad.data.PointRepository
import com.example.inroad.domain.entities.Point
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PointInteractor @Inject constructor() {
    private val repository: PointRepository = PointRepository()
    private val mapper: PointDataMapper = PointDataMapper()

    fun getPoints(latitude: Double, longitude: Double, minDistance: Int, maxDistance: Int): Observable<List<Point>> =
        repository.getPoints(latitude, longitude, minDistance, maxDistance).map {
        val result = mutableListOf<Point>();
        for (pointData in it) {
            val test = mapper.dataToDomain(pointData)
            result.add(test);
        }
        result
    }
}