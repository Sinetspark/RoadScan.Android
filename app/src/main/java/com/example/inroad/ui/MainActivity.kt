package com.example.inroad.ui

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.inroad.R
import com.example.inroad.databinding.ActivityMainBinding
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.managers.BumpManager
import com.example.inroad.managers.LocationManager
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var initPoints = false

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

        navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        val component = (applicationContext as AppComponentProvider).component
        component.inject(this)

        viewModel.pingData.observe(this) {
            state ->
            if (!state.success) {
                alertWithOk("Ошибка", "Извините, сервис временно недоступен")
            } else {
                initPoints = true
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
            } else {
                startBumps()
            }
        }

        viewModel.defaultConnection(this)
    }

    override fun onStart() {
        super.onStart()
        locationManager.onStart(this)
    }

    private fun startBumps() {
        bumpManager.onStart(this, locationManager)
        viewModel.onBumpWorkerStart(applicationContext)
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
