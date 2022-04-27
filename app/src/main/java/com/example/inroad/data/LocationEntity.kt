package com.example.inroad.data

data class LocationEntity(
    val latitude: Double,
    val longitude: Double
) {
    fun mapToDomain() : LocationDomainModel {
        return LocationDomainModel(
            latitude,
            longitude
        )
    }
}
