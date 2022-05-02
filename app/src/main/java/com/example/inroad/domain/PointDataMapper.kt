package com.example.inroad.domain

import com.example.inroad.data.dto.PointData
import com.example.inroad.domain.entities.Point

class PointDataMapper {
    fun dataToDomain(data: PointData): Point {
        return Point(data.latitude, data.longitude)
    }
}