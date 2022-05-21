package com.example.inroad.data.dto

import com.example.inroad.data.DataConstants
import com.google.gson.annotations.SerializedName

data class InsertBump(
    @SerializedName(DataConstants.KEY_LATITUDE)
    val latitude: Double,
    @SerializedName(DataConstants.KEY_LONGITUDE)
    val longitude: Double,
    @SerializedName(DataConstants.KEY_CITY)
    val city: String,
    @SerializedName(DataConstants.KEY_COUNTRY)
    val country: String
)