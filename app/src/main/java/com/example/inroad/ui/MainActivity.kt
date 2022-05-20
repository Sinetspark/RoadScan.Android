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
import com.google.android.gms.maps.model.MarkerOptions
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
    private var initPoints = false;

    @Inject
    lateinit var locationManager: LocationManager

//    @Inject
//    lateinit var accelerometerManager: AccelerometerManager

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
        viewModel.ping()
        viewModel.pingData.observe(this) {
            state ->
            if (!state.success) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Ошибка")
                builder.setMessage("Извините, сервис временно недоступен")
                builder.setPositiveButton("Oк",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                builder.show()
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
        locationManager.locations
            .subscribe { location ->
                if (mMap != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(location!!.latitude,
                            location!!.longitude), DEFAULT_ZOOM.toFloat()))
                    if (initPoints) {
//                        viewModel.getPoints(location.latitude, location.longitude, 0, 10000)
                    }
                    binding.locations.text = "${location.latitude}, ${location.longitude}"
                }
            }

        locationManager.speed
            .subscribe {
                speed ->
                binding.speed.text = "${speed} km/h ?"
            }

       /* bumpManager.onStart(this, locationManager)
        bumpManager.bumps
            .subscribe { locations ->
                Log.i("BumpLocation",
                    "${locations.latitude}, ${locations.longitude}")
            }*/
    }

    private fun startManagers() {
        locationManager.onStart(this)
        bumpManager.onStart(this, locationManager)
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
