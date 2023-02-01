package com.example.data.common.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class FlickrRetrofit @Inject constructor(
    private val retrofitBuilder: Retrofit.Builder,
    private val okHttpClientBuilder: OkHttpClient.Builder
) : RetrofitBuilder {

    companion object {
        private const val FLICKR_BASE_URL = "https://api.flickr.com/services/"
        const val API_KEY = "1508443e49213ff84d566777dc211f2a"
    }

    override val retrofit: Retrofit
        get() {
            val okHttpClient = okHttpClientBuilder.build()
            return retrofitBuilder
                .client(okHttpClient)
                .baseUrl(FLICKR_BASE_URL)
                .build()
        }
}