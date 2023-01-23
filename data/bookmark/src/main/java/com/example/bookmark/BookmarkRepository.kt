package com.example.bookmark

import com.example.data.common.database.bookmark.BookmarkDao
import com.example.data.common.database.bookmark.PhotoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BookmarkRepository {
    suspend fun addBookmark(photo: PhotoEntity)
    suspend fun removeBookmark(photo: PhotoEntity)
    fun getBookmarks(): Flow<List<PhotoEntity>>
}

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

    override fun getBookmarks(): Flow<List<PhotoEntity>> = bookmarkDao.getAll()

    override suspend fun addBookmark(photo: PhotoEntity) = bookmarkDao.insert(photo)

    override suspend fun removeBookmark(photo: PhotoEntity) = bookmarkDao.delete(photo)
}
