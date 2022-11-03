package com.example.inroad.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.inroad.R
import com.example.inroad.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity2  : AppCompatActivity() {


    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityMapsBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("isFirstRun", true)

        if (isFirstRun) {
            startActivity(Intent(this@MainActivity2, IntroSlidersActivity::class.java))
        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).commit()
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController=Navigation.findNavController(this, R.id.activity_main_nav_host_fragment)
        setupWithNavController(binding.bottomNavigationView,navController)

        enableCurrentLocation()
    }

    private fun enableCurrentLocation(){
        if (ContextCompat.checkSelfPermission(this@MainActivity2,
                Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity2,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@MainActivity2,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity2,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }

}