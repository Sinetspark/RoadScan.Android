package com.example.inroad.di

import android.content.Context
import com.example.inroad.MainApplication
import com.example.inroad.MyLocationManager
import com.example.inroad.ui.MainActivity
import com.example.inroad.ui.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(viewModel: MainViewModel)
    fun inject(app: MainApplication)

    fun myLocationManager(): MyLocationManager

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}