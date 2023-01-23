package com.example.data.common.database.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.common.database.DbConfig.SEARCH_HISTORIES_TABLE_NAME

@Entity(tableName = SEARCH_HISTORIES_TABLE_NAME)
data class SearchHistoryEntity(
    @PrimaryKey val text: String,
    val timestamp: String = System.currentTimeMillis().toString(),
)