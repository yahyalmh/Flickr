package com.example.detail.detail

import com.example.detail.api.DetailService
import com.example.detail.model.DetailResponse
import javax.inject.Inject

interface FlickrDetailRepository {
    suspend fun getPhotoDetail(photoId: String): DetailResponse
}

class FlickrDetailRepositoryImpl @Inject constructor(
    private val flickrSearchApi: DetailService
) : FlickrDetailRepository {

    override suspend fun getPhotoDetail(photoId: String) =
        flickrSearchApi.getPhotoDetail(photoId = photoId)
}