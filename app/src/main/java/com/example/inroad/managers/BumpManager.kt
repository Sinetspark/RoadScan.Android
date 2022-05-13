package com.example.inroad.managers

import android.content.Context
import androidx.activity.ComponentActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import android.location.Location
import io.reactivex.rxjava3.functions.Function
import java.util.*
import javax.inject.Inject

class BumpManager(
    context: Context
)  {

    lateinit var accelerometerManager: AccelerometerManager
    private val bumpSubject by lazy { BehaviorSubject.create<Float>() }
    val bumps: Observable<Float> = bumpSubject

    fun onStart(activity: ComponentActivity, locationManager: LocationManager) { 
        accelerometerManager = AccelerometerManager()
        accelerometerManager.onStart(activity)
        var test = listOf(
            locationManager.speed,
            locationManager.locations,
            accelerometerManager.spreads
        )
        val filter = Observable
            .combineLatest(test, Function {
                BumpTest(it[0] as Float, it[1] as Location, it[2] as FloatArray)
            })
            .filter { it.speed > 25 }
            // .buffer(2)
            .subscribe { bump ->
                bumpSubject.onNext(bump.speed)
            }

    }
}

data class BumpTest(val speed: Float, val locations: Location, val spreads: FloatArray)