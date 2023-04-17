package com.example.inroad.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.inroad.R
import com.example.inroad.data.dto.PointStatus
import com.example.inroad.di.AppComponentProvider
import com.example.inroad.domain.entities.PointType
import com.example.inroad.managers.LocationManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class MapsFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraIdleListener {
    private var currentLocation: Location? = null
    private val defaultZoom = 15
    private val maxDistance = 2000
    private val minDistance = 0
    private var cameraMove = false

    private lateinit var mMap: GoogleMap

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var viewModelFactory: MapViewModel.Factory
    private val viewModel: MapViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val component = (requireActivity().applicationContext as AppComponentProvider).component
        component.inject(this)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        viewModel.mapData.observe(viewLifecycleOwner) { state ->
            for (point in state.points) {
                var resourceId = R.drawable.circle_green
                if (point.type == PointType.MEDIUM) {
                    resourceId = R.drawable.circle_yellow
                }
                else if (point.type == PointType.LARGE) {
                    resourceId = R.drawable.circle_red
                }
                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(point.latitude, point.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(resourceId))
                )
            }
        }
        locationManager.locations.subscribe {
            location -> onLocationChange(location)
        }
        enableCurrentLocation()
    }

    private fun onLocationChange(location: Location) {
        if (!cameraMove) {
            mMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location!!.latitude,
                        location!!.longitude
                    ), defaultZoom.toFloat()
                )
            )
        }
    }

    fun getPoints(latitude: Double, longitude: Double) {
        viewModel.getPoints(
            latitude,
            longitude,
            minDistance,
            maxDistance
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.setMinZoomPreference(6f)
        //mMap.setMinZoomPreference(14f)

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
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraIdleListener(this);
    }

    @SuppressLint("MissingPermission")
    private fun enableCurrentLocation() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    mMap.isMyLocationEnabled = true
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    mMap.isMyLocationEnabled = true
                } else -> {
                    mMap.isMyLocationEnabled = false
                // No location access granted.
            }
            }
        }
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    override fun onCameraMoveStarted(p0: Int) {
        cameraMove = true
        Log.d("onCameraMoveStarted", "onCameraMoveStarted")
    }

    override fun onCameraIdle() {
        if (cameraMove) {
            val coords = mMap.cameraPosition.target
            Log.d("onCameraIdle", "onCameraIdle")
            val targetLocation = Location("") //provider name is unnecessary
            targetLocation.latitude = coords.latitude//your coords of course
            targetLocation.longitude = coords.longitude
            if (currentLocation == null || currentLocation!!.distanceTo(targetLocation) > maxDistance) {
                getPoints(targetLocation.latitude, targetLocation.longitude)
                currentLocation = targetLocation
            }
            cameraMove = false
        }
    }
}