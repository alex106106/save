package com.example.savethem.Model

import com.google.android.gms.maps.model.LatLng

//data class LatLngWrapper(
//	var latitude: Double? = 0.0,
//	var longitude: Double? = 0.0
//) {
//	constructor() : this(0.0, 0.0)
//}

data class ChatModel(
	var IDMessage: String? = "",
	var UUIDSender: String? = "",
	var message: String? = "",
//	var location: LatLngWrapper = LatLngWrapper(),
	var timestamp: Long? = 0
)
data class ChatLocationModel(
	var IDMessage: String? = "",
	var UUIDSender: String? = "",
	var message: String? = "",
	var location: LatLngWrapper? = null,
	var timestamp: Long? = 0
)

