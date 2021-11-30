package com.gmail.eamosse.idbdata.data

data class Movie(
    val adult: Boolean,
    val genres: List<Category>,
    val id: Int,
    val original_language: String,
    val poster_path: String,
    val production_companies: List<Director>,
    val title: String,
    val video: Boolean,
    val vote_average: Number,
    val vote_count: Int
)
