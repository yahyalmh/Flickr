package com.example.data.common.api

import retrofit2.Retrofit

interface RetrofitBuilder {

    val retrofit: Retrofit

    fun <T> create(service: Class<T>): T = retrofit.create(service)
}