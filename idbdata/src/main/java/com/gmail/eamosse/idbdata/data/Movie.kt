package com.gmail.eamosse.idbdata.data

data class Movie (
    val id: Int,
    val title: String,
    val genre_ids: List<Int>,
    val release_date: String,
    val video: Boolean,
    val poster_path: String
)