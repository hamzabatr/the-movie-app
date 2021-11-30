package com.gmail.eamosse.idbdata.api.response

import com.gmail.eamosse.idbdata.data.Category
import com.gmail.eamosse.idbdata.data.Director
import com.gmail.eamosse.idbdata.data.Movie
import com.google.gson.annotations.SerializedName

internal data class MovieResponse(
    @SerializedName("adult")
    val adult: Boolean,
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
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val vote_average: Number,
    @SerializedName("vote_count")
    val vote_count: Int
)

internal fun MovieResponse.toMovie() = Movie(
    adult,
    genres,
    id,
    original_language,
    poster_path,
    production_companies,
    title,
    video,
    vote_average,
    vote_count
)