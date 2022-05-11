package com.example.inroad.domain

import com.example.inroad.data.PointRepository
import com.example.inroad.domain.entities.Point
import io.reactivex.rxjava3.core.Observable

class BumpInteractor {
    private val repository: PointRepository = PointRepository()
    private val mapper: PointDataMapper = PointDataMapper()

    fun getPoints(): Observable<Array<Point>> = repository.getPoints().map {
        val result = arrayOf<Point>();
        for (pointData in it) {
            result.plus(mapper.dataToDomain(pointData));
        }
        return@map result;
    }

    fun postLocation(latitude: Double, longitude: Double) = repository.postLocation(latitude, longitude)
}