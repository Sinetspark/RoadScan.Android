package com.example.inroad.fragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.inroad.R
import com.example.inroad.ui.MainActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions


class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
        if (activity != null){
        val act = activity as MainActivity?}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
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

    }


}