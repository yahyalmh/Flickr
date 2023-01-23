package com.example.filckrsearch.api

import com.example.filckrsearch.model.FlickrResponse.*
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    companion object {
        const val API_KEY = "1508443e49213ff84d566777dc211f2a"
    }

    @GET("rest/?method=flickr.photos.search&safe_search=1&nojsoncallback=1&format=json")
    suspend fun search(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchResponse

    @GET("rest/?method=flickr.photos.getInfo&nojsoncallback=1&format=json")
    suspend fun getPhotoDetail(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("photo_id") photoId: String,
    ): DetailResponse
}