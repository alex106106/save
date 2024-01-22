package com.example.savethem.Model

import com.google.android.gms.maps.model.LatLng

data class MarkerModel(
    var ID: String? = "",
    var homicide: homicide? = homicide("",0.0),
    var kidnapping: kidnapping? = kidnapping("",0.0),
    var vehicleTheft: vehicleTheft? = vehicleTheft("",0.0),
    var rape: rape? = rape("",0.0),
    var femicide: femicide? = femicide("",0.0),
    var position: LatLng? = LatLng(0.0,0.0),
    var title: String? = "",
    var gLevel: String? = ""

){
    constructor() : this(
        "",
        homicide("",0.0),
        kidnapping("",0.0),
        vehicleTheft("",0.0),
        rape("",0.0),
        femicide("",0.0),
        LatLng(0.0,0.0),
        "",
        "")
}

data class homicide(
    var level: String,
    var percentage: Double
)
data class kidnapping(
    var level: String,
    var percentage: Double
)
data class vehicleTheft(
    var level: String,
    var percentage: Double
)
data class rape(
    var level: String,
    var percentage: Double
)
data class femicide(
    var level: String,
    var percentage: Double
)
