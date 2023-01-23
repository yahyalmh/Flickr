package com.example.bookmark.di

import com.example.bookmark.BookmarkRepository
import com.example.bookmark.BookmarkRepositoryImpl
import com.example.bookmark.BookmarksInteractor
import com.example.bookmark.BookmarksInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BookmarkModule {

    @Binds
    fun bindBookmarkRepository(bookmarkRepositoryImpl: BookmarkRepositoryImpl): BookmarkRepository

    @Binds
    fun bindBookmarkInteractor(bookmarksInteractorImpl: BookmarksInteractorImpl): BookmarksInteractor
}