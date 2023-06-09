package com.example.inroad.di

import android.content.Context
import com.example.inroad.domain.BumpInteractor
import com.example.inroad.managers.LocationManager
import com.example.inroad.domain.PointInteractor
import com.example.inroad.managers.BumpManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

//    @Provides
//    @Singleton
//    fun providesPointInteractor(): PointInteractor {
//        return PointInteractor()
//    }

    @Provides
    @Singleton
    fun providesLocationManager(context: Context): LocationManager {
        return LocationManager(context)
    }

    @Provides
    @Singleton
    fun providesBumpManager(context: Context): BumpManager {
        return BumpManager(context)
    }

    @Provides
    @Singleton
    fun providesBumpInteractor(): BumpInteractor {
        return BumpInteractor()
    }
}