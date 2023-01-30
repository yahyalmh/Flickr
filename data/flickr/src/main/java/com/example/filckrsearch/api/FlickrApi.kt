package com.example.filckrsearch.api

import com.example.filckrsearch.model.FlickrResponse.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    companion object {
        const val API_KEY = "1508443e49213ff84d566777dc211f2a"
    }

    @GET("rest/?method=flickr.photos.search&safe_search=1&nojsoncallback=1&format=json")
    suspend fun search(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): SearchResponse

    @GET("rest/?method=flickr.photos.search&safe_search=1&nojsoncallback=1&format=json")
    suspend fun search2(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
//        @Query("api_key") apiKey: String = API_KEY
    ): Response<SearchResponse>

    @GET("rest/?method=flickr.photos.getInfo&nojsoncallback=1&format=json")
    suspend fun getPhotoDetail(
        @Query("photo_id") photoId: String,
        @Query("api_key") apiKey: String = API_KEY,
    ): DetailResponse
}