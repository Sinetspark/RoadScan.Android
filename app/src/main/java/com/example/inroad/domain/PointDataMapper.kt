package com.example.inroad.domain

import com.example.inroad.data.dto.BumpData
import com.example.inroad.data.dto.PointData
import com.example.inroad.domain.entities.Point

class PointDataMapper {
    fun dataToDomain(data: PointData): Point {
        return Point(data.Id, data.latitude, data.longitude)
    }
}