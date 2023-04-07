package com.example.inroad.managers

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import android.location.Location
import android.util.Log
import com.example.inroad.managers.models.Bump
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class BumpManager(
    context: Context
)  {
    lateinit var accelerometerManager: AccelerometerManager
    private val bumpSubject by lazy { BehaviorSubject.create<Location>() }
    val bumps: Observable<Location> = bumpSubject

    fun onStart(activity: ComponentActivity, locationManager: LocationManager) {
        var previousSquare: Double? = null
        accelerometerManager = AccelerometerManager()
        accelerometerManager.onStart(activity)
        var observers = listOf(
            locationManager.speed,
            locationManager.locations,
            accelerometerManager.spreads
        )
        Observable
            .combineLatest(observers, Function {
                Bump(it[0] as Float, it[1] as Location, it[2] as FloatArray)
            })
            .filter { it.speed > 5 } // todo km/h
            .subscribeOn(Schedulers.io())
            .subscribe { bump ->
                var spreads = bump.spreads
                var currentSquare = sqrt(spreads[0].toDouble().pow(2.0) + spreads[1].toDouble()
                    .pow(2.0) + spreads[2].toDouble().pow(2.0)
                )
                if (previousSquare != null) {
                    var result = abs(previousSquare!!) - abs(currentSquare)
                    Log.i("result", "${result}")
                    if (abs(result) > 20) {
                        bumpSubject.onNext(bump.locations)
                        Log.i("resultBump", "${result}")
                    }
                }
                previousSquare = currentSquare
            }

    }
}