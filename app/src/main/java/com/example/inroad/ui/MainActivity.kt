package com.example.inroad.ui

//import com.shashank.sony.fancytoastlib.FancyToast
import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.inroad.R
import com.example.inroad.databinding.ActivityMainBinding
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.managers.BumpManager
import com.example.inroad.managers.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var currentLocation: Location? = null
    private val defaultZoom = 15
    private var initPoints = false
    private val maxDistance = 10000
    private val minDistance = 0
    private val accelerometerPermissionKey = "accelerometerPermissionKey"

    @Inject

    lateinit var locationManager: LocationManager

    @Inject
    lateinit var bumpManager: BumpManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("isFirstRun", true)

        if (isFirstRun) {
            startActivity(Intent(this@MainActivity, IntroSlidersActivity::class.java))
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).commit()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController= Navigation.findNavController(this, R.id.activity_main_nav_host_fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        enableCurrentLocation()

        val component = (applicationContext as AppComponentProvider).component
        component.inject(this)


    }

    private fun accelerometerPermissionRequested(){
        val sharedPref = this.getPreferences(MODE_PRIVATE)
        val accelerometerPermissionRequested = sharedPref?.contains(accelerometerPermissionKey)
        if (accelerometerPermissionRequested == true) {
            val accelerometerPermitted = readBooleanFromStorage(accelerometerPermissionKey)
            if (accelerometerPermitted) {
                startBumps()
            }
        } else {
            alertAccelerometer("Разрешить приложению отправлять данные аккселерометра?")
        }
    }

    override fun onStart() {
        super.onStart()
        locationManager.onStart(this)
        locationManager.locations
            .subscribe { location ->
                /*if (mMap != null) {
                    if (initPoints) {
                        if (currentLocation == null) {
                            currentLocation = location
                            viewModel.getPoints(location.latitude, location.longitude, minDistance, maxDistance)
                        }
                        else {
                            var distance = currentLocation!!.distanceTo(location)
                            if (distance > 2000) {
                                currentLocation = location
                                viewModel.getPoints(location.latitude, location.longitude, minDistance, maxDistance)
                            }
                        }
                    }
//                    binding.locations.text = "Локация: ${location.latitude}, ${location.longitude}"
                    *//* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(location!!.latitude,
                            location!!.longitude), defaultZoom.toFloat()))*//*
                }*/
            }
        locationManager.speed
            .subscribe {
                speed ->
                //binding.speed.text = "Скорость: ${speed} km/h"
            }
       /* bumpManager.bumps
            .subscribe{
                bumpLocation ->
                FancyToast.makeText(this,"Неровность обнаружена",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show()
            }*/
    }

    private fun startBumps() {
        bumpManager.onStart(this, locationManager)
        //viewModel.onBumpWorkerStart(applicationContext)
    }


    private fun enableCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
    }


/*    fun accelerometrAccess(private val context: Context) {
        private val settings: SharedPreferences
        private val keyAccelereometr = "access"
        fun setAppInPurchasedMode(status: String) {
            if (status == "successful") {
                settings.edit().putBoolean(keyAccelereometr, true).commit()
            } else if (status == "failed") {
                settings.edit().putBoolean(keyAccelereometr, false).commit()
            }
        }

        fun accelerometrIsAccess(): Boolean {
            var access = false
            if (settings.getBoolean(keyAccelereometr, false)) {
                access = true
            }
            return access
        }

        init {
            settings = PreferenceManager.getDefaultSharedPreferences(context)
        }
    }*/

    private fun saveBooleanToStorage(key: String, value: Boolean) {
        val sharedPref = this?.getPreferences(MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    private fun readBooleanFromStorage(key: String): Boolean {
        val sharedPref = this.getPreferences(MODE_PRIVATE)
//        if (sharedPref != null) {
//            return sharedPref?.getBoolean(key, false)
//        }
        return false
    }

    private fun alertAccelerometer(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Разрешить",
            DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this, "Спасибо за разрешение!", Toast.LENGTH_SHORT).show()
                startBumps()
                saveBooleanToStorage(accelerometerPermissionKey, true)
            })
        builder.setNegativeButton("Отклонить",
            DialogInterface.OnClickListener { dialog, which ->
                saveBooleanToStorage(accelerometerPermissionKey, false)
                dialog.cancel()
            })
        builder.show()
    }


}
