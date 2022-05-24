package com.example.inroad.managers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class LocationManager (
    context: Context
) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val locationSubject by lazy { BehaviorSubject.create<Location>() }
    private val speedSubject by lazy { BehaviorSubject.create<Float>() }

    val locations: Observable<Location> = locationSubject
    val speed: Observable<Float> = speedSubject

    fun onStart(activity: ComponentActivity) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val locationPermissionRequest = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                        Toast.makeText(activity, "Спасибо за разрешение!", Toast.LENGTH_SHORT).show()
                        setLocationListener()
                    }
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                        Toast.makeText(activity, "Примерно - это неплохо, но хотелось бы точнее", Toast.LENGTH_SHORT).show()
                        setLocationListener()
                    }
                    else -> {
                        Toast.makeText(activity, "Нам очень необходимо это разрешение", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        } else {
            setLocationListener()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLocationListener() {
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationSubject.onNext(location)
                speedSubject.onNext((location.speed * 3600) / 1000)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?)
            {
                Log.i("LocationListener", "StatusChanged")
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1f, listener)
    }
}