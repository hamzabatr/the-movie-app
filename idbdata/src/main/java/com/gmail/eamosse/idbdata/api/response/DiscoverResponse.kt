package com.gmail.eamosse.idbdata.api.response

import com.gmail.eamosse.idbdata.data.Movie
import com.google.gson.annotations.SerializedName

internal data class DiscoverResponse(
    @SerializedName("with_genres")
    val results: List<Movie>
) {
    data class Movie(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String
    )
}

internal fun DiscoverResponse.Movie.toMovie() = Movie(
    id = id,
    title = title
)
