package com.example.history

import com.example.data.common.database.history.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface SearchHistoryInteractor {
    fun getHistories(): Flow<List<SearchHistoryEntity>>
    suspend fun addHistory(historyText: String)
    suspend fun removeHistory(searchHistoryEntity: SearchHistoryEntity)
    suspend fun removeHistory(historyText: String)
}

class SearchHistoryInteractorImpl @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
) : SearchHistoryInteractor {

    override fun getHistories(): Flow<List<SearchHistoryEntity>> =
        searchHistoryRepository.getHistories()

    override suspend fun addHistory(historyText: String) {
        historyText.takeIf { it.isNotEmpty() && it.isNotBlank() }?.let {
            searchHistoryRepository.addHistory(SearchHistoryEntity(it))
        }
    }

    override suspend fun removeHistory(searchHistoryEntity: SearchHistoryEntity) =
        searchHistoryRepository.removeHistory(searchHistoryEntity)

    override suspend fun removeHistory(historyText: String) {
        getHistories().first().firstOrNull {
            it.text == historyText
        }?.let { searchHistoryRepository.removeHistory(it) }
    }
}