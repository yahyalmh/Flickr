package com.example.data.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.common.database.bookmark.BookmarkDao
import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.database.history.SearchHistoryDao
import com.example.data.common.database.history.SearchHistoryEntity

@Database(version = 1, entities = [PhotoEntity::class, SearchHistoryEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}