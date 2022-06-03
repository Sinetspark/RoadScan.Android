package com.example.inroad.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.inroad.R
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.inroad.databinding.ActivityMapsBinding
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.managers.BumpManager
import com.example.inroad.managers.LocationManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private val defaultZoom = 15
    private var initPoints = false
    private val maxDistance = 10000
    private val minDistance = 0

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory
    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var bumpManager: BumpManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val component = (applicationContext as AppComponentProvider).component
        component.inject(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel.pingData.observe(this) {
            state ->
            if (!state.success) {
                alertWithOk("Ошибка", "Извините, сервис временно недоступен")
            } else {
                startManagers()
                viewModel.onBumpManagerStart(applicationContext)
                initPoints = true
            }
        }
        viewModel.mapData.observe(this) { state ->
            for (point in state.points) {
                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(point.latitude, point.longitude))
                )
            }
        }
        viewModel.ping()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val connectivityManager = this.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, viewModel.networkCallback)

        viewModel.connectionData.observe(this) { isConnected ->
            if (!isConnected) {
                alertWithOk("Связь", "У вас пропал интернет")
            }
        }

        viewModel.defaultConnection(this)

    }

    override fun onStart() {
        super.onStart()
        locationManager.onStart(this)
        locationManager.locations
            .subscribe { location ->
                if (mMap != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(location!!.latitude,
                            location!!.longitude), defaultZoom.toFloat()))
                    if (initPoints) {
                        viewModel.getPoints(location.latitude, location.longitude, minDistance, maxDistance)
                    }
                    binding.locations.text = "Локация: ${location.latitude}, ${location.longitude}"
                }
            }
        locationManager.speed
            .subscribe {
                speed ->
                binding.speed.text = "Скорость: ${speed} km/h"
            }
    }

    private fun startManagers() {
        bumpManager.onStart(this, locationManager)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.setMinZoomPreference(6f)
        mMap.setMinZoomPreference(14f)

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style_json
                )
            )
            if (!success) {
                // Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            // Log.e(TAG, "Can't find style. Error: ", e)
        }
        enableCurrentLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay! Do the
            // location-related task you need to do.
            enableCurrentLocation()
        }
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true
    }

    private fun alertWithOk(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Oк",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }
}
