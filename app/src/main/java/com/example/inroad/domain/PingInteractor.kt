package com.example.inroad.domain

import com.example.inroad.data.PingRepository
import com.example.inroad.data.dto.InsertBumps
import io.reactivex.rxjava3.core.Observable

class PingInteractor {
    private val repository: PingRepository = PingRepository()

    fun getPing(): Observable<Void> = repository.getPing()
}

