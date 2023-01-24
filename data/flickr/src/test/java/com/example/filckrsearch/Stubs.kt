package com.example.filckrsearch

import com.example.data.common.ext.RandomString
import com.example.filckrsearch.model.*
import com.example.filckrsearch.model.FlickrResponse.DetailResponse
import com.example.filckrsearch.model.FlickrResponse.SearchResponse
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
    description = DescriptionModel( RandomString()),
    visibility = Visibility(isPublic = 1, isFriend = 1, isFamily = 1),
    dates = DatesModel(),
    tags = Tags(),
    isFavorite = Random().nextInt(),
    dateUploaded = System.currentTimeMillis().toString()
)


fun flickrSearchResponse(photos: PhotosModel = photosModelStub()) = SearchResponse(
    photos = photos,
    stat = RandomString()
)

fun photoModelListStub(count: Int = 10): List<PhotoModel> {
    val result = mutableListOf<PhotoModel>()
    repeat(count) {
        result.add(photoModelStub())
    }
    return result
}

fun photosModelStub() = PhotosModel(
    page = "1",
    perPage = "25",
    pages = "234",
    total = "23243",
    photoItems = photoModelListStub()
)

fun photoModelStub() = PhotoModel(
    id = RandomString(),
    owner = RandomString(),
    secret = RandomString(),
    server = RandomString(),
    farm = RandomString(),
    title = RandomString(),
    isPublic = RandomString(),
    isFriend = RandomString(),
    isFamily = RandomString(),
)