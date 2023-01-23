package com.example.data.common.model

import com.example.data.common.ImageType
import com.example.data.common.ImageType.*
import com.example.data.common.database.bookmark.PhotoEntity

data class PhotoDetail(
    val id: String,
    val secret: String,
    val server: String,
    val description: String,
    val isFamily: Boolean,
    val isFavorite: Boolean,
    val isPublic: Boolean,
    val isFriend: Boolean,
    val views: Int,
    val title: String?,
    val usage: Usage,
    val owner: Owner,
    val dates: Dates,
    val tags: List<String?> = arrayListOf(),
){
    fun getImageUrl(type: ImageType = MEDIUM_640) =
        "https://live.staticflickr.com/${server}/${id}_${secret}_${type.value}.jpg"
}

data class Owner(
    val username: String,
    val location: String? = null,
    val realName: String? = null,
)

data class Dates(
    val posted: String? = null,
    val taken: String? = null,
    val lastUpdate: String? = null,
    val uploadData: String
)

data class Usage(
    val canDownload: Boolean,
    val canBlog: Boolean,
    val canPrint: Boolean,
    val canShare: Boolean
)

fun PhotoDetail.toEntity(
    localAddress: String,
    imageType: ImageType = LARGE_SQUARE,
    timestamp: String = System.currentTimeMillis().toString()
) = PhotoEntity(
        id = id,
        title = title ?: "",
        imageUrl = getImageUrl(imageType),
        localAddress = localAddress,
        timestamp = timestamp,
    )