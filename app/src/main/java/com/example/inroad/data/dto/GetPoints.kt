package com.example.inroad.data.dto

import com.example.inroad.data.DataConstants
import com.google.gson.annotations.SerializedName

data class GetPoints(
    @SerializedName(DataConstants.KEY_LATITUDE)
    val latitude: Double,
    @SerializedName(DataConstants.KEY_LONGITUDE)
    val longitude: Double,
    @SerializedName(DataConstants.KEY_MIN_DISTANCE)
    val minDistance: Int,
    @SerializedName(DataConstants.KEY_MAX_DISTANCE)
    val maxDistance: Int,
)