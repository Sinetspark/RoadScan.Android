package com.example.inroad.managers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.util.Log
import androidx.activity.ComponentActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AccelerometerManager {
    private lateinit var sensorManager: SensorManager
    private val accelerometerSubject by lazy { BehaviorSubject.create<FloatArray>() }
    val spreads: Observable<FloatArray> = accelerometerSubject

    fun onStart(activity: ComponentActivity) {
        sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val listener = object : SensorEventListener {
            override fun onAccuracyChanged(arg0: Sensor?, arg1: Int) {

            }
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    accelerometerSubject.onNext(floatArrayOf(event.values[0], event.values[1], event.values[2]))
                }
            }
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            accelerometer ->
                sensorManager.registerListener(listener, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
    }
}