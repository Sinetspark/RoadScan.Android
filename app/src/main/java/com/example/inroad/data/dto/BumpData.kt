package com.example.inroad.data.dto

import com.example.inroad.data.DataConstants
import com.google.gson.annotations.SerializedName

data class BumpData (
    @SerializedName(DataConstants.KEY_ID)
    val Id: String,
    @SerializedName(DataConstants.KEY_LATITUDE)
    val latitude: Double,
    @SerializedName(DataConstants.KEY_LONGITUDE)
    val longitude: Double,
)