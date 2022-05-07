package com.example.inroad.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.inroad.MyLocationManager
import com.example.inroad.R
import com.example.inroad.databinding.ActivityMainBinding
import com.example.inroad.di.AppComponentProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var mMap: GoogleMap

    private var currentMarker: Marker? = null
    private val DEFAULT_ZOOM = 15

    @Inject
    lateinit var myLocationManager: MyLocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val component = (applicationContext as AppComponentProvider).component
        component.inject(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        /*binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Сначала создаем подписку на изменение состояния экрана
       *//* viewModel.liveData.observe(this) { state ->
            binding.allMaterialtoolbarTopbar.title = state.title
            binding.allTextviewTemperature.text = state.temperature
            binding.allTextviewWeather.setText(state.weatherTextId)
            binding.root.setBackgroundColor(state.backgroundColor)
        }*//*

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
        myLocationManager.onStart(this)
        myLocationManager.locations
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
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
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


