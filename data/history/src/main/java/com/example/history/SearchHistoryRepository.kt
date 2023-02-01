package com.example.history

import com.example.data.common.database.history.SearchHistoryDao
import com.example.data.common.database.history.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchHistoryRepository {
    fun getHistories(): Flow<List<SearchHistoryEntity>>
    suspend fun addHistory(searchHistoryEntity: SearchHistoryEntity)
    suspend fun removeHistory(searchHistoryEntity: SearchHistoryEntity)
}

class SearchHistoryRepositoryImpl @Inject constructor(
    private val historyDao: SearchHistoryDao
) : SearchHistoryRepository {
    override fun getHistories(): Flow<List<SearchHistoryEntity>> = historyDao.getAll()
    override suspend fun addHistory(searchHistoryEntity: SearchHistoryEntity) =
        historyDao.insert(searchHistoryEntity)

    override suspend fun removeHistory(searchHistoryEntity: SearchHistoryEntity) =
        historyDao.delete(searchHistoryEntity)
}
