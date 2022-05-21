package com.example.inroad.data.dto

import com.example.inroad.data.DataConstants
import com.google.gson.annotations.SerializedName

data class InsertBumps (
    @SerializedName(DataConstants.KEY_BUMPS)
    val bumps: ArrayList<InsertBump>
)