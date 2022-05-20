package com.example.inroad.domain

import com.example.inroad.data.PingRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject


class PingInteractor @Inject constructor() {
    private val repository: PingRepository = PingRepository()

    fun getPing(): Observable<Void> = repository.getPing()
}

