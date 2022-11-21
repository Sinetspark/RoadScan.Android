package com.example.inroad.fragments

import android.content.DialogInterface
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.inroad.R
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.ui.MainViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var initPoints = false

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory
    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val component = (activity?.applicationContext as AppComponentProvider).component
        component.inject(this)

        viewModel.pingData.observe(viewLifecycleOwner) {
                state ->
            if (!state.success) {
                alertWithOk("Ошибка", "Извините, сервис временно недоступен")
            } else {
                //accelerometerPermissionRequested()
                initPoints = true
            }
        }

        viewModel.mapData.observe(viewLifecycleOwner) { state ->
            for (point in state.points) {
                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(point.latitude, point.longitude))
                )
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val connectivityManager = activity?.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, viewModel.networkCallback)
        viewModel.connectionData.observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                alertWithOk("Связь", "У вас пропал интернет")
            }
        }

        viewModel.defaultConnection(activity!!)

        viewModel.ping()
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
                  context?.let {
                      MapStyleOptions.loadRawResourceStyle(
                          it, R.raw.style_json
                      )
                  }
              )
              if (!success) {
                  // Log.e(TAG, "Style parsing failed.")
              }
          } catch (e: Resources.NotFoundException) {
              // Log.e(TAG, "Can't find style. Error: ", e)
            }
        mMap.isMyLocationEnabled = true
        }

    private fun alertWithOk(title: String, message: String) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(title)
        builder?.setMessage(message)
        builder?.setPositiveButton("Oк",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder?.show()
    }
}