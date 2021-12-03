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
        @SerializedName("poster_path")
        val poster_path: String
    )

}

internal fun MoviesResponse.Movies.toMovies() = Movies(
    id = id,
    poster_path = poster_path,
)