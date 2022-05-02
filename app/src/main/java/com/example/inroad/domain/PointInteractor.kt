package com.example.inroad.domain

import android.content.Context
import com.example.inroad.data.PointRepository
import com.example.inroad.data.dto.PointData
import com.example.inroad.domain.entities.Point
import io.reactivex.rxjava3.core.Observable

class PointInteractor constructor(context: Context) {
    private val repository: PointRepository = PointRepository()
    private val mapper: PointDataMapper = PointDataMapper()

    fun getPoints(): Observable<Array<Point>> = repository.getPoints().map {
        val result = arrayOf<Point>();
        for (pointData in it) {
            result.plus(mapper.dataToDomain(pointData));
        }
        return@map result;
    }
}