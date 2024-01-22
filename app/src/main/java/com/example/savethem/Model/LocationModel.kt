package com.example.savethem.Model

data class LatLngWrapper(
	var latitude: Double? = 0.0,
	var longitude: Double? = 0.0
) {
	constructor() : this(0.0, 0.0)
}

data class LocationModel(
	var IDMessage: String? = "",
	var UUIDSender: String? = "",
	var location: LatLngWrapper = LatLngWrapper(),
	var timestamp: Long? = 0
)