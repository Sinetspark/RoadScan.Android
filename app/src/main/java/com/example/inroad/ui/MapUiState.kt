package com.example.inroad.ui

import com.example.inroad.domain.entities.Point

data class MapUiState(
    val points: List<Point>
)

data class PingState(
    val success: Boolean
)