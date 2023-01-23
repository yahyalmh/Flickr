package com.example.data.common.database.history

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM histories order by timestamp DESC")
    fun getAll(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(searchHistoryEntity: SearchHistoryEntity)

    @Delete
    suspend fun delete(searchHistoryEntity: SearchHistoryEntity)
}