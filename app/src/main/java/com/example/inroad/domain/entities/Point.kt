package com.example.inroad.domain.entities

data class Point(val id: String, val latitude: Double, val longitude: Double, val type: PointType)

enum class PointType(val value: Int) {
    SMALL(0),
    MEDIUM(1),
    LARGE(2);

    companion object {
        fun fromInt(value: Int) = PointType.values().first { it.value == value }
    }
}