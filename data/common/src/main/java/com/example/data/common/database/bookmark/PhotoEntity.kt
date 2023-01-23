package com.example.data.common.database.bookmark

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.common.database.DbConfig.BOOKMARK_TABLE_NAME

@Entity(tableName = BOOKMARK_TABLE_NAME)
data class PhotoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val localAddress: String,
    val timestamp: String,
)