package com.example.filckrsearch.model

import com.google.gson.annotations.SerializedName

data class PhotosModel(
    val page: String,
    @SerializedName("perpage")
    val perPage: String,
    val pages: String,
    val total: String,
    @SerializedName("photo")
    val photoItems: List<PhotoModel>
)