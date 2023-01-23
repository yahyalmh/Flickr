package com.example.data.common.model

import com.example.data.common.ImageType
import com.example.data.common.ImageType.*
import com.example.data.common.database.bookmark.PhotoEntity

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: String,
    val title: String,
    val isPublic: String,
    val isFriend: String,
    val isFamily: String,
) {
    fun getImageUrl(type: ImageType = THUMBNAIL) =
        "https://live.staticflickr.com/${server}/${id}_${secret}_${type.value}.jpg"
}

fun Photo.toEntity(
    localAddress: String,
    imageType: ImageType = LARGE_SQUARE,
    timestamp: String = System.currentTimeMillis().toString()
) =
    PhotoEntity(
        id = id,
        title = title,
        imageUrl = getImageUrl(imageType),
        localAddress = localAddress,
        timestamp = timestamp,
    )