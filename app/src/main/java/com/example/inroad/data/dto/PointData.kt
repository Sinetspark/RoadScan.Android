package com.example.inroad.data.dto

import com.google.gson.annotations.SerializedName
import com.example.inroad.data.DataConstants

data class PointData(
    @SerializedName(DataConstants.KEY_LATITUDE)
    val latitude: Double,
    @SerializedName(DataConstants.KEY_LONGITUDE)
    val longitude: Double,
)