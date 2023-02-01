package com.example.search.search

import com.example.search.model.PhotosModel
import com.example.search.api.SearchService
import javax.inject.Inject

interface FlickrSearchRepository {
    suspend fun search(query: String, page: Int, perPage: Int): PhotosModel
}

class FlickrSearchRepositoryImpl @Inject constructor(
    private val flickrSearchApi: SearchService
) : FlickrSearchRepository {

    override suspend fun search(query: String, page: Int, perPage: Int): PhotosModel =
        flickrSearchApi.search(query = query, page = page, perPage = perPage).photos

}