package com.example.savethem.Model

data class likeModel(
    var IDComments: String? = "",
    var UUIDComments: String? ="",
    var likes: MutableMap<String, Boolean> = mutableMapOf()
){
    constructor() : this(
        "",
        "")
}