package com.example.inroad.data

import com.example.inroad.data.dto.GetPoints
import com.example.inroad.data.dto.InsertBumps
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface BumpService {
    @POST("Bump/InsertBumps")
    fun insertBumps(@Body insertBumps: InsertBumps): Observable<Void>
}