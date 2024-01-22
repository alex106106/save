package com.example.savethem.Model

import com.google.android.gms.maps.model.LatLng

data class CommentsModel(
    var IDComments: String? = "",
    var UUIDComments: String? ="",
    var content: String? = "",
    var name: String? = "",
    var likes: List<String> = emptyList()
){
    constructor() : this(
        "",
        "",
        "")
}