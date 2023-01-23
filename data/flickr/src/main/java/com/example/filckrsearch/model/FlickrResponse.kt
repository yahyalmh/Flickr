package com.example.filckrsearch.model


sealed class FlickrResponse {
    data class SearchResponse(
        val stat: String,
        val photos: PhotosModel
    ) : FlickrResponse()

    data class DetailResponse(
        val stat: String,
        val photo: PhotoDetailModel
    ) : FlickrResponse()
}
