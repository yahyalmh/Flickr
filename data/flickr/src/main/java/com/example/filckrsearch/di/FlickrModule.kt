package com.example.filckrsearch.di

import com.example.data.common.api.FlickrRetrofit
import com.example.filckrsearch.api.FlickrApi
import com.example.filckrsearch.detail.FlickrDetailInteractor
import com.example.filckrsearch.detail.FlickrDetailInteractorImpl
import com.example.filckrsearch.detail.FlickrDetailRepository
import com.example.filckrsearch.detail.FlickrDetailRepositoryImpl
import com.example.filckrsearch.search.FlickrSearchInteractor
import com.example.filckrsearch.search.FlickrSearchInteractorImpl
import com.example.filckrsearch.search.FlickrSearchRepository
import com.example.filckrsearch.search.FlickrSearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FlickrModule {

    companion object {
        @Provides
        fun provideFlickrSearchApi(flickrRetrofit: FlickrRetrofit): FlickrApi =
            flickrRetrofit.create(FlickrApi::class.java)
    }

    @Binds
    fun bindFlickrSearchRepository(
        flickrSearchRepositoryImpl: FlickrSearchRepositoryImpl
    ): FlickrSearchRepository

    @Binds
    fun bindFlickrSearchInteractor(
        flickrSearchInteractorImpl: FlickrSearchInteractorImpl
    ): FlickrSearchInteractor

    @Binds
    fun bindFlickrDetailRepository(
        flickrDetailRepositoryImpl: FlickrDetailRepositoryImpl
    ): FlickrDetailRepository

    @Binds
    fun bindFlickrDetailInteractor(
        flickrDetailInteractorImpl: FlickrDetailInteractorImpl
    ): FlickrDetailInteractor
}