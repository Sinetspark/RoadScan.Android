package com.example.inroad.data.dto

import com.google.gson.annotations.SerializedName
import com.example.inroad.data.DataConstants
import com.example.inroad.domain.entities.PointType

data class PointData(
    @SerializedName(DataConstants.KEY_ID)
    val Id: String,
    @SerializedName(DataConstants.KEY_LATITUDE)
    val latitude: Double,
    @SerializedName(DataConstants.KEY_LONGITUDE)
    val longitude: Double,
    @SerializedName(DataConstants.KEY_POINTTYPE)
    val type: Int,
)