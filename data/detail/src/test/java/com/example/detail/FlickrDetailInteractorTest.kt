package com.example.detail

import com.example.detail.detail.FlickrDetailInteractorImpl
import com.example.detail.detail.FlickrDetailRepositoryImpl
import com.example.detail.model.toExternalModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
internal class FlickrDetailInteractorTest {

    @Mock
    lateinit var flickrDetailRepository: FlickrDetailRepositoryImpl
    private lateinit var flickrDetailInteractor: FlickrDetailInteractorImpl

    @BeforeEach
    fun setUp() {
        flickrDetailInteractor = FlickrDetailInteractorImpl(flickrDetailRepository)
    }

    @Test
    fun `WHEN  get a photo detail THEN  return detail`() = runTest {
        val photoDetailModel = photoDetailModel()
        val photoId = photoDetailModel.id
        val flickrResponse = flickrDetailResponse(photo = photoDetailModel)
        whenever(flickrDetailRepository.getPhotoDetail(photoId)).thenReturn(flickrResponse)

        val actual = flickrDetailInteractor.getPhotoDetail(photoId).first()
        val expected = photoDetailModel.toExternalModel()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `WHEN repository throw error THEN  interactor pass the error`() =
        runTest {
            val photoDetailModel = photoDetailModel()
            whenever(flickrDetailRepository.getPhotoDetail(photoDetailModel.id)).thenThrow(
                RuntimeException()
            )

            assertThrows<RuntimeException> {
                flickrDetailInteractor.getPhotoDetail(photoDetailModel.id).first()
            }
        }
}