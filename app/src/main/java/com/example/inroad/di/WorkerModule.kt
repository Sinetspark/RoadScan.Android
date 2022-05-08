package com.example.inroad.di

import android.content.Context
import androidx.work.WorkerParameters
import com.example.inroad.managers.LocationManager
import com.example.inroad.workers.TestWorker
import dagger.Module
import dagger.Provides

@Module
class WorkerModule {

    @Provides
    fun providesLocationWorker(context: Context, workerParameters: WorkerParameters, locationManager: LocationManager): TestWorker = TestWorker(
        appContext = context,
        workerParams = workerParameters,
        locationManager = locationManager,
    )
}