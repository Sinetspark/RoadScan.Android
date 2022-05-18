package com.example.inroad.managers.models

import android.location.Location

data class Bump(val speed: Float, val locations: Location, val spreads: FloatArray)