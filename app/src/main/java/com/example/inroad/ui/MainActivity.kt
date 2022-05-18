package com.example.inroad.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.inroad.R
import com.example.inroad.databinding.ActivityMainBinding
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.managers.AccelerometerManager
import com.example.inroad.managers.BumpManager
import com.example.inroad.managers.LocationManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var mMap: GoogleMap

    private var currentMarker: Marker? = null
    private val DEFAULT_ZOOM = 15

    @Inject
    lateinit var locationManager: LocationManager

//    @Inject
//    lateinit var accelerometerManager: AccelerometerManager

    @Inject
    lateinit var bumpManager: BumpManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val component = (applicationContext as AppComponentProvider).component
        component.inject(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        viewModel.liveData.observe(this) { state ->
////            binding.allMaterialtoolbarTopbar.title = state.title
////            binding.allTextviewTemperature.text = state.temperature
////            binding.allTextviewWeather.setText(state.weatherTextId)
////            binding.root.setBackgroundColor(state.backgroundColor)
//            for (point in state.points) {
//                mMap.addMarker(
//                    MarkerOptions()
//                        .position(LatLng(point.latitude, point.longitude))
//                )
//            }
//        }

        /*binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Сначала создаем подписку на изменение состояния экрана

        // Затем вызываем у viewModel колбэк onCreated оповещая viewModel что подготовительные работы завершены
        // Эта проверка на savedInstanceState здесь нужна чтобы onInitiallyCreated вызвался только при первом старте экрана
        if (savedInstanceState == null) {
            viewModel.onInitiallyCreated(component, applicationContext)
        }
        binding.allButtonService.setOnClickListener {
            viewModel.onServiceButtonClicked(applicationContext)
        }*/
    }

    override fun onStart() {
        super.onStart()
        locationManager.onStart(this)
        locationManager.locations
            .subscribe { location ->
                if (mMap != null) {
//                    val markerOptions = MarkerOptions()
//                    val latLng = LatLng(location.latitude, location.longitude)
//                    markerOptions.position(latLng)
//                    markerOptions.title("Current Position")
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//                    currentMarker?.remove()
//                    currentMarker = mMap.addMarker(markerOptions)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(location!!.latitude,
                            location!!.longitude), DEFAULT_ZOOM.toFloat()))
                   // viewModel.getPoints(applicationContext, 5.0, 24.0, 0, 10000)
                }
            }
//        accelerometerManager.onStart(this)
//        accelerometerManager.spreads
//            .subscribe { spread ->
//                Log.i("SensorChanged",
//                    "${spread[0]}, ${spread[1]}, ${spread[2]}")
       //     }
        bumpManager.onStart(this, locationManager)
        bumpManager.bumps
            .subscribe { locations ->
                Log.i("BumpLocation",
                    "${locations.latitude}, ${locations.longitude}")
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
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
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.setMinZoomPreference(6f)
        mMap.setMinZoomPreference(14f)
    }
}

/*
private var sensorManager: SensorManager? = null
var ax = 0.0
var ay = 0.0
var az = 0.0 // these are the acceleration in x,y and z axis

public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    sensorManager = getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
    sensorManager!!.registerListener(
        this,
        sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_NORMAL
    )
}

override fun onAccuracyChanged(arg0: Sensor?, arg1: Int) {}
override fun onSensorChanged(event: SensorEvent) {
    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
        ax = event.values[0].toDouble()
        ay = event.values[1].toDouble()
        az = event.values[2].toDouble()

        */
/*Log.d(ax.toString(), ay.toString())*//*

    }
}
*/


