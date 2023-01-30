package com.example.filckrsearch.search

import com.example.filckrsearch.Result
import com.example.filckrsearch.model.PhotosModel
import com.example.filckrsearch.api.FlickrApi
import com.example.filckrsearch.asResult
import com.example.filckrsearch.model.FlickrResponse
import javax.inject.Inject

interface FlickrSearchRepository {
    suspend fun search(query: String, page: Int, perPage: Int): PhotosModel
    suspend fun search2(
        query: String,
        page: Int,
        perPage: Int
    ): Result<FlickrResponse.SearchResponse>
}

class FlickrSearchRepositoryImpl @Inject constructor(
    private val flickrSearchApi: FlickrApi
) : FlickrSearchRepository {

    override suspend fun search(query: String, page: Int, perPage: Int): PhotosModel =
        flickrSearchApi.search(query = query, page = page, perPage = perPage).photos

    override suspend fun search2(query: String, page: Int, perPage: Int) =
        flickrSearchApi.search2(query = query, page = page, perPage = perPage).asResult()

}