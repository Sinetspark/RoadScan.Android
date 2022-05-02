package com.example.inroad

import android.app.Application
import com.example.inroad.di.AppComponent
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.di.DaggerAppComponent

class MainApplication : Application(), AppComponentProvider {

    private var _component: AppComponent? = null
    override val component: AppComponent
        get() = _component!!

    override fun onCreate() {
        super.onCreate()
        _component = DaggerAppComponent.factory().create(this)
    }
}