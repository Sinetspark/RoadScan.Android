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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.inroad.R
import com.example.inroad.databinding.ActivityMainBinding
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.managers.BumpManager
import com.example.inroad.managers.LocationManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var mMap: GoogleMap
    private var currentLocation: Location? = null
    private val defaultZoom = 15
    private var initPoints = false
    private val maxDistance = 10000
    private val minDistance = 0
    private val accelerometerPermissionKey = "accelerometerPermissionKey"

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
        /*val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)*/

        viewModel.pingData.observe(this) {
            state ->
            if (!state.success) {
                alertWithOk("Ошибка", "Извините, сервис временно недоступен")
            } else {
                accelerometerPermissionRequested()
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
                if (mMap != null) {
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
                    /* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(location!!.latitude,
                            location!!.longitude), defaultZoom.toFloat()))*/
                }
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
        viewModel.onBumpWorkerStart(applicationContext)
    }

    fun enableCurrentLocation() {
        /*if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = false*/

        if (ActivityCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
            mMap.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay! Do the
            // location-related task you need to do.
            enableCurrentLocation()
        }
    }

/*    override fun onMapReady(googleMap: GoogleMap) {
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
    }*/

    private fun alertWithOk(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Oк",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
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
