package com.example.inroad.di

import android.content.Context
import com.example.inroad.MyLocationManager
import com.example.inroad.domain.PointInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun providesPointInteractor(context: Context): PointInteractor {
        return PointInteractor(context)
    }

    @Provides
    @Singleton
    fun providesMyLocationManager(context: Context): MyLocationManager {
        return MyLocationManager(context)
    }
}