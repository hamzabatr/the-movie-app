package com.gmail.eamosse.idbdata.api.response

import com.gmail.eamosse.idbdata.data.Movies
import com.google.gson.annotations.SerializedName

internal data class MoviesResponse(

    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Movies>,
    @SerializedName("total_results")
    val total_results: Int,
    @SerializedName("total_pages")
    val total_pages: Int
) {
    data class Movies(
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
        val poster_path: String,
        @SerializedName("popularity")
        val popularity: Number,
        @SerializedName("vote_average")
        val vote_average: Number,
        @SerializedName("original_language")
        val original_language: String,
        @SerializedName("adult")
        val adult: Boolean
    )

}

internal fun MoviesResponse.Movies.toMovies() = Movies(
    id = id,
    title = title,
    genre_ids = genre_ids,
    release_date = release_date,
    video = video,
    poster_path = poster_path,
    popularity = popularity,
    vote_average = vote_average,
    original_language = original_language,
    adult = adult
)