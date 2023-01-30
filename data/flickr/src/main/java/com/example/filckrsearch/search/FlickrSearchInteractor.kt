package com.example.filckrsearch.search

import com.example.data.common.model.Photo
import com.example.filckrsearch.model.toExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface FlickrSearchInteractor {
    fun search(query: String, page: Int , perPage: Int): Flow<List<Photo>>
}

class FlickrSearchInteractorImpl @Inject constructor(
    private val flickrSearchRepository: FlickrSearchRepository
) : FlickrSearchInteractor {

    override fun search(query: String, page: Int, perPage: Int) = flow {
        val result = flickrSearchRepository.search(
            query = query,
            page = page,
            perPage = perPage
        )
        val result2 = flickrSearchRepository.search2(
            query = query,
            page = page,
            perPage = perPage
        )
        emit(result.photoItems.map { it.toExternalModel() })
    }.flowOn(Dispatchers.IO)
}