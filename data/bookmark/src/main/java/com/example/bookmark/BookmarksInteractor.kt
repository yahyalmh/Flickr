package com.example.bookmark

import com.example.data.common.database.bookmark.PhotoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BookmarksInteractor {
    fun getBookmarks(): Flow<List<PhotoEntity>>
    suspend fun addBookmark(photoEntity: PhotoEntity)
    suspend fun removeBookmark(photoEntity: PhotoEntity)
}

class BookmarksInteractorImpl @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) : BookmarksInteractor {

    override fun getBookmarks(): Flow<List<PhotoEntity>> =
        bookmarkRepository.getBookmarks()

    override suspend fun addBookmark(photoEntity: PhotoEntity) =
        bookmarkRepository.addBookmark(photoEntity)

    override suspend fun removeBookmark(photoEntity: PhotoEntity) =
        bookmarkRepository.removeBookmark(photoEntity)
}