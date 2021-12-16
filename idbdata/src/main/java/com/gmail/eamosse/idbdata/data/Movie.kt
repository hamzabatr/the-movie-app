package com.gmail.eamosse.idbdata.data

data class Movie(
    val backdrop_path: String,
    val genres: List<Category>,
    val id: Int,
    val original_language: String,
    val poster_path: String,
    val production_companies: List<Director>,
    val title: String,
    val vote_average: Number,
    val release_date: String
)