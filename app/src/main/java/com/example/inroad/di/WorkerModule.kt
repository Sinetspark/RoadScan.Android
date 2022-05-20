package com.example.inroad.di

import android.content.Context
import androidx.work.WorkerParameters
import com.example.inroad.managers.BumpManager
import com.example.inroad.managers.LocationManager
import com.example.inroad.workers.BumpWorker
import dagger.Module
import dagger.Provides

@Module
class WorkerModule {

    @Provides
    fun providesLocationWorker(context: Context, workerParameters: WorkerParameters, bumpManager: BumpManager): BumpWorker = BumpWorker(
        appContext = context,
        workerParams = workerParameters,
        bumpManager = bumpManager,
    )
}