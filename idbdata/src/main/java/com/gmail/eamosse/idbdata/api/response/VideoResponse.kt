package com.gmail.eamosse.idbdata.api.response

import com.gmail.eamosse.idbdata.data.Video
import com.google.gson.annotations.SerializedName

internal data class VideoResponse(
    @SerializedName("results")
    val results: List<Video>,
) {
    data class Video(
        @SerializedName("id")
        val id: String,
        @SerializedName("key")
        val key: String,
        @SerializedName("site")
        val site: String
    )
}

internal fun VideoResponse.Video.toVideo() = Video(
    id = id,
    key = key,
    site = site
)
