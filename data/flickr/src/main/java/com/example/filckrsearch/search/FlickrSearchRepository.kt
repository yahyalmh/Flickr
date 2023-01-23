package com.example.filckrsearch.search

import com.example.filckrsearch.model.PhotosModel
import com.example.filckrsearch.api.FlickrApi
import javax.inject.Inject

interface FlickrSearchRepository {
    suspend fun search(query: String, page: Int, perPage: Int): PhotosModel
}

class FlickrSearchRepositoryImpl @Inject constructor(
    private val flickrSearchApi: FlickrApi
) : FlickrSearchRepository {

    override suspend fun search(query: String, page: Int, perPage: Int): PhotosModel =
        flickrSearchApi.search(query = query, page = page, perPage = perPage).photos

}