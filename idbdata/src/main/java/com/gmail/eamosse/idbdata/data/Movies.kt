package com.gmail.eamosse.idbdata.data

data class Movies(
    val id: Int,
    val poster_path: String,
    val title: String,
    var favorite: Boolean
)

