package com.example.filckrsearch.model

import com.example.data.common.model.Dates
import com.example.data.common.model.Owner
import com.example.data.common.model.PhotoDetail
import com.example.data.common.model.Usage
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class PhotoDetailModel(
    val id: String,
    val secret: String,
    val server: String,
    val views: Int,
    val owner: OwnerModel,
    val title: Title,
    val usage: UsageModel,
    val description: DescriptionModel,
    val visibility: Visibility,
    val dates: DatesModel,
    val tags: Tags,
    @SerializedName("isfavorite") val isFavorite: Int,
    @SerializedName("dateuploaded") val dateUploaded: String,
)

data class DescriptionModel(
    @SerializedName("_content") val Content: String
)

data class OwnerModel(
    val username: String,
    val location: String? = null,
    @SerializedName("realname") val realName: String? = null,
)

fun OwnerModel.toExternalModel() = Owner(username, location, realName)

data class Title(
    @SerializedName("_content") val Content: String? = null
)

data class DatesModel(
    val posted: String? = null,
    val taken: String? = null,
    @SerializedName("lastupdate") val lastUpdate: String? = null
)

data class Tags(
    val tag: ArrayList<Tag> = arrayListOf()
)

data class Tag(
    val id: String? = null,
    val author: String? = null,
    val raw: String? = null,
    val Content: String? = null,
    val machineTag: String? = null,
    @SerializedName("authorname") val authorName: String? = null

)

data class UsageModel(
    @SerializedName("candownload") val canDownload: Int,
    @SerializedName("canblog") val canBlog: Int,
    @SerializedName("canprint") val canPrint: Int,
    @SerializedName("canshare") val canShare: Int
)

fun UsageModel.toExternalModel() = Usage(
    canDownload = canDownload.toBoolean(),
    canBlog = canBlog.toBoolean(),
    canPrint = canPrint.toBoolean(),
    canShare = canShare.toBoolean(),
)

data class Visibility(
    @SerializedName("ispublic") val isPublic: Int,
    @SerializedName("isfriend") val isFriend: Int,
    @SerializedName("isfamily") val isFamily: Int
)

fun PhotoDetailModel.toExternalModel() = PhotoDetail(
    id = id,
    secret = secret,
    server = server,
    views = views,
    owner = owner.toExternalModel(),
    title = title.Content,
    usage = usage.toExternalModel(),
    description = description.Content,
    dates = Dates(
        posted = dates.posted?.toLong()?.toDate(),
        taken = dates.taken,
        lastUpdate = dates.lastUpdate?.toLong()?.toDate(),
        uploadData = dateUploaded.toLong().toDate()
    ),
    tags = tags.tag.mapNotNull { it.raw },
    isFavorite = isFavorite.toBoolean(),
    isPublic = visibility.isPublic.toBoolean(),
    isFriend = visibility.isFriend.toBoolean(),
    isFamily = visibility.isFamily.toBoolean()
)


private fun Long.toDate(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(this))

private fun Int.toBoolean() = this >= 1