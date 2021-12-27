package com.gmail.eamosse.idbdata.api.response

import com.gmail.eamosse.idbdata.data.Actor
import com.google.gson.annotations.SerializedName

internal data class ActorsResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Actors>,
    @SerializedName("total_results")
    val total_results: Int,
    @SerializedName("total_pages")
    val total_pages: Int
) {
    data class Actors(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}

internal fun ActorsResponse.Actors.toActor() = Actor(
    id = id,
    name = name
)
