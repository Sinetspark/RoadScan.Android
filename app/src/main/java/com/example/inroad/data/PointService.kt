package com.example.inroad.data

import com.example.inroad.data.dto.PointData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.POST

interface PointService {
    @POST("/Point/GetPoints")
    fun getPoints(): Observable<Array<PointData>>
}