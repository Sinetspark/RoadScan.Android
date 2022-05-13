package com.example.inroad.data

import com.example.inroad.data.dto.GetPoints
import com.example.inroad.data.dto.PointData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface PointService {
    @POST("Point/GetPoints")
    fun getPoints(@Body getPoints: GetPoints): Observable<Array<PointData>>
}