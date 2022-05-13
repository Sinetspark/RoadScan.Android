package com.example.inroad.data

import com.example.inroad.data.dto.InsertBumps
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable

class BumpRepository {
    private val api = BumpApi()
    private val gson = Gson()

    fun insertBumps(insertBumps: InsertBumps): Observable<Void> = api.insertBumps(insertBumps)
}