package com.example.inroad

import android.app.Application
import androidx.work.Configuration
import com.example.inroad.di.AppComponent
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.di.DaggerAppComponent
import io.reactivex.rxjava3.internal.functions.Functions.emptyConsumer
import io.reactivex.rxjava3.plugins.RxJavaPlugins

class MainApplication : Application(), AppComponentProvider, Configuration.Provider {

    private var _component: AppComponent? = null
    override val component: AppComponent
        get() = _component!!

    override fun onCreate() {
        super.onCreate()
        _component = DaggerAppComponent.factory().create(this)
        component.inject(this)
        RxJavaPlugins.setErrorHandler(emptyConsumer())
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(WorkerProvider(component))
            .build()
}