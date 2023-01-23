package com.example.filckrsearch

import com.example.data.common.ext.RandomString
import com.example.filckrsearch.api.FlickrApi
import com.example.filckrsearch.search.FlickrSearchRepository
import com.example.filckrsearch.search.FlickrSearchRepositoryImpl
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
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class FlickrSearchRepositoryTest {
    @Mock
    lateinit var flickrApi: FlickrApi
    private lateinit var flickrSearchRepository: FlickrSearchRepository

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        flickrSearchRepository = FlickrSearchRepositoryImpl(flickrApi)
    }

    @Test
    fun `WHEN search photos by repository THEN work well`() = runTest {
        // given
        val photosModel = photosModelStub()
        val flickrResponse = flickrSearchResponse(photos = photosModel)
        whenever(flickrApi.search(any(), any(), any(), any())).thenReturn(flickrResponse)

        // when
        val result =
            flickrSearchRepository.search(query = RandomString.next(), page = 1, perPage = 25)

        // then
        Assertions.assertEquals(flickrResponse.photos, result)
    }
}
