package com.example.inroad.domain

import android.content.Context
import com.example.inroad.data.BumpRepository
import com.example.inroad.domain.entities.Bump
import io.reactivex.rxjava3.core.Observable

class BumpInteractor constructor(context: Context){
    private val repository: BumpRepository = BumpRepository()
    private val mapper: BumpDataMapper = BumpDataMapper()

    fun getBumps(): Observable<Array<Bump>> = repository.getPoints().map {
        val result = arrayOf<Bump>();
        for (bumpData in it) {
            result.plus(mapper.dataToDomain(bumpData));
        }
        return@map result;
    }

    fun postLocation(latitude: Double, longitude: Double) = repository.postLocation(latitude, longitude)
}