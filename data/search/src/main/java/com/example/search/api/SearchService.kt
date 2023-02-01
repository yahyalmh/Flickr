package com.example.search.api

import com.example.data.common.api.FlickrRetrofit.Companion.API_KEY
import com.example.search.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("rest/?method=flickr.photos.search&safe_search=1&nojsoncallback=1&format=json")
    suspend fun search(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): SearchResponse

}