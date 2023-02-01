package com.example.detail

import com.example.data.common.ext.RandomString
import com.example.detail.model.DatesModel
import com.example.detail.model.DescriptionModel
import com.example.detail.model.DetailResponse
import com.example.detail.model.OwnerModel
import com.example.detail.model.PhotoDetailModel
import com.example.detail.model.Tags
import com.example.detail.model.Title
import com.example.detail.model.UsageModel
import com.example.detail.model.Visibility
import java.util.*


fun flickrDetailResponse(photo: PhotoDetailModel = photoDetailModel()) = DetailResponse(
    photo = photo,
    stat = RandomString()
)

fun photoDetailModel() = PhotoDetailModel(
    id = RandomString(),
    secret = RandomString(),
    server = RandomString(),
    views = Random().nextInt(),
    owner = OwnerModel(RandomString()),
    title = Title(),
    usage = UsageModel(canDownload = 1, canBlog = 1, canPrint = 1, canShare = 1),
    description = DescriptionModel(RandomString()),
    visibility = Visibility(isPublic = 1, isFriend = 1, isFamily = 1),
    dates = DatesModel(),
    tags = Tags(),
    isFavorite = Random().nextInt(),
    dateUploaded = System.currentTimeMillis().toString()
)