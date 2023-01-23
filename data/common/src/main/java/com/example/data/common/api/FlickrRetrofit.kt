package com.example.data.common.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * @author yaya (@yahyalmh)
 * @since 25th November 2022
 */

class FlickrRetrofit @Inject constructor(
    private val retrofitBuilder: Retrofit.Builder,
    private val okHttpClientBuilder: OkHttpClient.Builder
) : RetrofitBuilder {

    companion object {
        private const val FLICKR_BASE_URL = "https://api.flickr.com/services/"
    }

    override val retrofit: Retrofit
        get() {
            val okHttpClient = okHttpClientBuilder.addInterceptor(Interceptor { chain ->
                val request = chain
                    .request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request = request)
            }).build()

            return retrofitBuilder
                .client(okHttpClient)
                .baseUrl(FLICKR_BASE_URL)
                .build()
        }
}