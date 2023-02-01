package com.example.search

import com.example.data.common.ext.RandomString
import com.example.search.model.PhotoModel
import com.example.search.model.PhotosModel
import com.example.search.model.SearchResponse


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