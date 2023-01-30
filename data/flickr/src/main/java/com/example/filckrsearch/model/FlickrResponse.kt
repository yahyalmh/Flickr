package com.example.filckrsearch.model

import com.google.gson.annotations.SerializedName


open class FlickrResponse {
    @SerializedName("stat")
    open val status: String = ""

    data class SearchResponse(
        val photos: PhotosModel,
    ) : FlickrResponse()

    data class DetailResponse(
        val photo: PhotoDetailModel
    ) : FlickrResponse()

    data class ErrorResponse(
        val code: Int,
        val message: String,
    ):FlickrResponse()
}
