package com.example.search.model

import com.example.data.common.model.Photo
import com.google.gson.annotations.SerializedName

data class PhotoModel(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: String,
    val title: String,
    @SerializedName("ispublic")
    val isPublic: String,
    @SerializedName("isfriend")
    val isFriend: String,
    @SerializedName("isfamily")
    val isFamily: String,
)

fun PhotoModel.toExternalModel() = Photo(
    id = id,
    owner = owner,
    secret = secret,
    server = server,
    farm = farm,
    title = title,
    isPublic = isPublic,
    isFriend = isFriend,
    isFamily = isFamily
)