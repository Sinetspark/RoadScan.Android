package com.example.inroad.di

import android.content.Context
import androidx.work.WorkerParameters
import com.example.inroad.workers.BumpWorker
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Component(
    modules = [WorkerModule::class],
    dependencies = [AppComponent::class]
)
@WorkerComponent.WorkerScope
interface WorkerComponent {

    fun bumpWorker(): BumpWorker

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent,
            @BindsInstance workerParameters: WorkerParameters,
            @BindsInstance appContext: Context
        ): WorkerComponent
    }

    @Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class WorkerScope
}