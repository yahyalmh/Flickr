package com.example.detail.detail

import com.example.data.common.model.PhotoDetail
import com.example.detail.model.toExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface FlickrDetailInteractor {
    fun getPhotoDetail(photoId: String): Flow<PhotoDetail>
}

class FlickrDetailInteractorImpl @Inject constructor(
    private val flickrDetailRepository: FlickrDetailRepository
) : FlickrDetailInteractor {

    override fun getPhotoDetail(photoId: String) = flow {
        emit(flickrDetailRepository.getPhotoDetail(photoId).photo.toExternalModel())
    }.flowOn(Dispatchers.IO)
}