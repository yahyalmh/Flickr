package com.example.detail.api

import com.example.data.common.api.FlickrRetrofit.Companion.API_KEY
import com.example.detail.model.DetailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailService {

    @GET("rest/?method=flickr.photos.getInfo&nojsoncallback=1&format=json")
    suspend fun getPhotoDetail(
        @Query("photo_id") photoId: String,
        @Query("api_key") apiKey: String = API_KEY,
    ): DetailResponse
}