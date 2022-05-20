package com.example.inroad.ui

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.inroad.R
import com.example.inroad.databinding.ActivityMapsBinding
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.managers.BumpManager
import com.example.inroad.managers.LocationManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding

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
        // setContentView(R.layout.activity_maps)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val component = (applicationContext as AppComponentProvider).component
        component.inject(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.ping()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Ошибка")
                builder.setMessage("Извините, сервис недоступен")
                builder.setPositiveButton("Oк",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                val alert = builder.create()
                alert.show()
            });
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

        // Сначала создаем подписку на изменение состояния экрана

        // Затем вызываем у viewModel колбэк onCreated оповещая viewModel что подготовительные работы завершены
        // Эта проверка на savedInstanceState здесь нужна чтобы onInitiallyCreated вызвался только при первом старте экрана
//        if (savedInstanceState == null) {
//            viewModel.onInitiallyCreated(component, applicationContext)
//        }
//        binding.allButtonService.setOnClickListener {
//            viewModel.onServiceButtonClicked(applicationContext)
//        }*/
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
                    binding.locations.text = "${location.latitude}, ${location.longitude}"
                }
            }

        locationManager.speed
            .subscribe {
                speed ->
                binding.speed.text = "${speed} km/h ?"
            }

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


