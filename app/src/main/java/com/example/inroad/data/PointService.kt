package com.example.inroad.data

import com.example.inroad.data.dto.BumpData
import com.example.inroad.data.dto.PointData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface PointService {
    @POST("/Point/GetPoints")
    fun getPoints(): Observable<Array<PointData>>

    @POST
    fun getBumps(): Observable<Array<BumpData>>

    @POST("/location")
    fun postLocatiton(@Body location: String): Completable
}