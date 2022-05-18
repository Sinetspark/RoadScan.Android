package com.example.inroad.data

import io.reactivex.rxjava3.core.Observable


class PingRepository {
    private val api = PingApi()

    fun getPing(): Observable<Void> = api.getPing()
}