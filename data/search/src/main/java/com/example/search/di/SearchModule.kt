package com.example.search.di

import com.example.data.common.api.FlickrRetrofit
import com.example.search.api.SearchService
import com.example.search.search.FlickrSearchInteractor
import com.example.search.search.FlickrSearchInteractorImpl
import com.example.search.search.FlickrSearchRepository
import com.example.search.search.FlickrSearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SearchModule {

    companion object {
        @Provides
        fun provideFlickrSearchService(flickrRetrofit: FlickrRetrofit): SearchService =
            flickrRetrofit.create(SearchService::class.java)
    }

    @Binds
    fun bindFlickrSearchRepository(
        flickrSearchRepositoryImpl: FlickrSearchRepositoryImpl
    ): FlickrSearchRepository

    @Binds
    fun bindFlickrSearchInteractor(
        flickrSearchInteractorImpl: FlickrSearchInteractorImpl
    ): FlickrSearchInteractor

}