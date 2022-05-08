package com.example.inroad.managers

import android.Manifest
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

    val locations: Observable<Location> = locationSubject

    fun onStart(activity: ComponentActivity) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            val locationPermissionRequest = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                        Toast.makeText(activity, "Спасибо за разрешение!", Toast.LENGTH_SHORT).show()
                    }
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                        Toast.makeText(activity, "Примерно - это неплохо, но хотелось бы точнее", Toast.LENGTH_SHORT).show()
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
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationSubject.onNext(location)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?)
            {
                Log.i("LocationListener", "StatusChanged")
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1f, listener)
    }
}