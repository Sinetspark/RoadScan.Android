package com.example.inroad.di

import android.content.Context
import androidx.work.WorkerParameters
import com.example.inroad.MyLocationManager
import com.example.inroad.workers.TestWorker
import dagger.Module
import dagger.Provides

@Module
class WorkerModule {

    @Provides
    fun providesLocationWorker(context: Context, workerParameters: WorkerParameters, myLocationManager: MyLocationManager): TestWorker = TestWorker(
        appContext = context,
        workerParams = workerParameters,
        myLocationManager = myLocationManager,
    )
}