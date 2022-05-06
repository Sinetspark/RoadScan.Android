package com.example.inroad

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.inroad.di.AppComponent
import com.example.inroad.di.DaggerWorkerComponent
import com.example.inroad.workers.TestWorker

class WorkerProvider constructor(private val appComponent: AppComponent) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerComponent = DaggerWorkerComponent.factory().create(appComponent, workerParameters, appContext)

        return when(workerClassName) {
            TestWorker::class.qualifiedName -> workerComponent.locationWorker()
            else -> null
        }
    }
}