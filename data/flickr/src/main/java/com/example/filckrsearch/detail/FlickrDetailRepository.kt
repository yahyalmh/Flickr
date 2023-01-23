package com.example.filckrsearch.detail

import com.example.filckrsearch.api.FlickrApi
import com.example.filckrsearch.model.FlickrResponse.*
import javax.inject.Inject

interface FlickrDetailRepository {
    suspend fun getPhotoDetail(photoId: String): DetailResponse
}

class FlickrDetailRepositoryImpl @Inject constructor(
    private val flickrSearchApi: FlickrApi
) : FlickrDetailRepository {

    override suspend fun getPhotoDetail(photoId: String) =
        flickrSearchApi.getPhotoDetail(photoId = photoId)
}