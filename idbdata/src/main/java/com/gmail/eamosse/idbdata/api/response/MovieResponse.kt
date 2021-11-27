package com.gmail.eamosse.idbdata.api.response

import com.gmail.eamosse.idbdata.data.Movie
import com.google.gson.annotations.SerializedName

internal data class MovieResponse(
    @SerializedName("results")
    val results: List<Movie>
) {
    data class Movie(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("genre_ids")
        val genre_ids: List<Int>,
        @SerializedName("release_date")
        val release_date: String,
        @SerializedName("video")
        val video: Boolean,
        @SerializedName("poster_path")
        val poster_path: String
    )
}

internal fun MovieResponse.Movie.toMovie() = Movie(
    id = id,
    title = title,
    genre_ids = genre_ids,
    release_date = release_date,
    video = video,
    poster_path = poster_path

)