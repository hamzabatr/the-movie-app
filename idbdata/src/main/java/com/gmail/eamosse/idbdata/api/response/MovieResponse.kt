package com.gmail.eamosse.idbdata.api.response

import com.gmail.eamosse.idbdata.data.Category
import com.gmail.eamosse.idbdata.data.Director
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("genres")
    val genres: List<Category>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_language")
    val original_language: String,
    @SerializedName("poster_path")
    val poster_path: String,
    @SerializedName("production_companies")
    val production_companies: List<Director>,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val vote_average: Number,
    @SerializedName("release_date")
    val release_date: String,
    @SerializedName("backdrop_path")
    val backdrop_path: String
)

internal fun MovieResponse.toMovie() = MovieResponse(
    genres,
    id,
    original_language,
    poster_path,
    production_companies,
    title,
    vote_average,
    release_date,
    backdrop_path
)
