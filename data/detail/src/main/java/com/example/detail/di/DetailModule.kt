package com.example.detail.di

import com.example.data.common.api.FlickrRetrofit
import com.example.detail.api.DetailService
import com.example.detail.detail.FlickrDetailInteractor
import com.example.detail.detail.FlickrDetailInteractorImpl
import com.example.detail.detail.FlickrDetailRepository
import com.example.detail.detail.FlickrDetailRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DetailModule {

    companion object {
        @Provides
        fun provideFlickrDetailService(flickrRetrofit: FlickrRetrofit): DetailService =
            flickrRetrofit.create(DetailService::class.java)
    }

    @Binds
    fun bindFlickrDetailRepository(
        flickrDetailRepositoryImpl: FlickrDetailRepositoryImpl
    ): FlickrDetailRepository

    @Binds
    fun bindFlickrDetailInteractor(
        flickrDetailInteractorImpl: FlickrDetailInteractorImpl
    ): FlickrDetailInteractor
}