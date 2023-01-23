package com.example.data.common.database.bookmark

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks order by timestamp DESC")
    fun getAll(): Flow<List<PhotoEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(photoEntities: List<PhotoEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(photoEntity: PhotoEntity)

    @Delete
    suspend fun delete(photoEntity: PhotoEntity)
}