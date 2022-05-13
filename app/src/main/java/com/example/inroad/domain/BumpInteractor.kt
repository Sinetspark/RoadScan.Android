package com.example.inroad.domain

import android.content.Context
import com.example.inroad.data.BumpRepository
import com.example.inroad.data.dto.InsertBumps
import com.example.inroad.domain.entities.Bump
import io.reactivex.rxjava3.core.Observable

class BumpInteractor constructor(context: Context) {
    private val repository: BumpRepository = BumpRepository()
    private val mapper: BumpDataMapper = BumpDataMapper()

    fun insertBumps(insertBumps: InsertBumps): Observable<Void> = repository.insertBumps(insertBumps)
}