package com.example.history.di

import com.example.history.SearchHistoryInteractor
import com.example.history.SearchHistoryInteractorImpl
import com.example.history.SearchHistoryRepository
import com.example.history.SearchHistoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface SearchHistoryModule {

    @Binds
    fun bindSearchHistoryRepository(repository: SearchHistoryRepositoryImpl): SearchHistoryRepository

    @Binds
    fun bindSearchHistoryInteractor(searchHistoryInteractorImpl: SearchHistoryInteractorImpl): SearchHistoryInteractor
}