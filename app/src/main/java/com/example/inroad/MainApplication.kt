package com.example.inroad

import android.app.Application
import androidx.work.Configuration
import com.example.inroad.di.AppComponent
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.di.DaggerAppComponent

class MainApplication : Application(), AppComponentProvider, Configuration.Provider {

    private var _component: AppComponent? = null
    override val component: AppComponent
        get() = _component!!

    override fun onCreate() {
        super.onCreate()
        _component = DaggerAppComponent.factory().create(this)
        component.inject(this)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(WorkerProvider(component))
            .build()
}