package com.example.inroad.domain

import com.example.inroad.data.dto.PointData
import com.example.inroad.domain.entities.Point
import com.example.inroad.domain.entities.PointType

class PointDataMapper {
    fun dataToDomain(data: PointData): Point {
        return Point(data.Id, data.latitude, data.longitude, PointType.fromInt(data.type))
    }
}