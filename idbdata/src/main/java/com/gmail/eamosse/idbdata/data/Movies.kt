package com.gmail.eamosse.idbdata.data

data class Movies(

    val poster_path: String,
    val adult: Boolean,
    val release_date: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val title: String,
    val popularity: Number,
    val video: Boolean,
    val vote_average: Number
)
