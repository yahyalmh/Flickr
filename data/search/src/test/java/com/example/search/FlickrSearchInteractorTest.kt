package com.example.search

import com.example.data.common.ext.RandomString
import com.example.search.model.toExternalModel
import com.example.search.search.FlickrSearchInteractorImpl
import com.example.search.search.FlickrSearchRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
internal class FlickrSearchInteractorTest {

    @Mock
    lateinit var flickrSearchRepository: FlickrSearchRepositoryImpl
    private lateinit var flickrSearchInteractor: FlickrSearchInteractorImpl

    @BeforeEach
    fun setUp() {
        flickrSearchInteractor = FlickrSearchInteractorImpl(flickrSearchRepository)
    }

    @Test
    fun `WHEN fetch search result form interactor THEN return data`() = runTest {
        val photosModel = photosModelStub()
        whenever(flickrSearchRepository.search(any(), any(), any())).thenReturn(photosModel)

        val actual = flickrSearchInteractor.search(
            query = RandomString(),
            page = 1,
            perPage = 25
        ).first()

        val expected = photosModel.photoItems.map { it.toExternalModel() }

        Assertions.assertEquals(expected, actual)
    }
}