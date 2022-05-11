package com.example.inroad.domain

import com.example.inroad.data.dto.BumpData
import com.example.inroad.domain.entities.Bump

class BumpDataMapper {
    fun dataToDomain(data: BumpData): Bump {
        return Bump(data.Id, data.latitude, data.longitude)
    }
}