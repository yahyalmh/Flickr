package com.example.detail

import com.example.detail.api.DetailService
import com.example.detail.detail.FlickrDetailRepository
import com.example.detail.detail.FlickrDetailRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class FlickrSearchRepositoryTest {
    @Mock
    lateinit var detailService: DetailService
    private lateinit var flickrDetailRepository: FlickrDetailRepository

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        flickrDetailRepository = FlickrDetailRepositoryImpl(detailService)
    }

    @Test
    fun `WHEN a photo details fetched from repository THEN return details`() = runTest {
        // given
        val photoDetailModel = photoDetailModel()
        val photoId = photoDetailModel.id
        val flickrResponse = flickrDetailResponse(photo = photoDetailModel)

        whenever(detailService.getPhotoDetail(photoId)).thenReturn(flickrResponse)

        // when
        val result = flickrDetailRepository.getPhotoDetail(photoId)

        // then
        Assertions.assertEquals(photoDetailModel, result.photo)
    }
}
