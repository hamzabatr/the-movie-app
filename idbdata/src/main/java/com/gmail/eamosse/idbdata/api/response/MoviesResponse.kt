package com.gmail.eamosse.idbdata.api.response

import com.google.gson.annotations.SerializedName

data class MoviesResponse(

    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Movies>,
    @SerializedName("total_pages")
    val total_pages: Int
) {
    data class Movies(
        @SerializedName("id")
        val id: Int,
        @SerializedName("poster_path")
        val poster_path: String,
        @SerializedName("title")
        val title: String
    )
}

internal fun MoviesResponse.toResponse() = MoviesResponse(
    page,
    results,
    total_pages
)
