package com.example.simplegallery.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.simplegallery.data.remote.models.Location
import com.example.simplegallery.data.remote.models.PhotoRemote
import com.example.simplegallery.data.remote.models.Urls
import com.example.simplegallery.data.remote.models.UserData
import com.example.simplegallery.data.remote.services.PhotoService
import com.example.simplegallery.data.repository.PhotoPagingSource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PhotoPagingSourceTest {

    private lateinit var photoService: PhotoService
    private lateinit var pagingSource: PhotoPagingSource

    @Before
    fun setUp() {
        photoService = mockk()
        pagingSource = PhotoPagingSource(photoService)
    }

    @Test
    fun `load returns Page on successful response`() = runTest {
        val photos = listOf(photoRemote1, photoRemote2)
        val response = Response.success(photos)

        coEvery { photoService.getPhotos(page = 1, perPage = 20) } returns response

        //When
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        //Then
        val expected = PagingSource.LoadResult.Page(
            data = photos,
            prevKey = null,
            nextKey = 2
        )

        assert(loadResult == expected)
    }

    @Test
    fun `load returns Error on unsuccessful response`() = runTest {
        //Given
        val errorResponse = Response.error<List<PhotoRemote>>(500, "".toResponseBody("application/json".toMediaTypeOrNull()))

        coEvery { photoService.getPhotos(page = 1, perPage = 20) } returns errorResponse

        //When
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        //Then
        assert(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals("Failed to load photos", error.throwable.message)
    }

    @Test
    fun `load returns Error on exception`() = runTest {
        //Given
        coEvery { photoService.getPhotos(page = 1, perPage = 20) } throws IOException("Network error")

        //When
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        //Assert
        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals("Network error", error.throwable.message)
    }

    @Test
    fun `getRefreshKey returns correct key`() {
        //Given
        val items = listOf(photoRemote1, photoRemote2, photoRemote3)

        val state = PagingState(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = items,
                    prevKey = 1,
                    nextKey = 2
                )
            ),
            anchorPosition = 1,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        //When
        val key = pagingSource.getRefreshKey(state)

        //Then
        assertEquals(2, key)
    }
}

val photoRemote1 = PhotoRemote(
    id = "1",
    urls = Urls(
        raw = "",
        full = "",
        regular = "https://example.com/photo1",
        small = "",
    ),
    location = Location(
        city = "",
        country = ""
    ),
    user = UserData(
        id = "",
        username = "",
        name = "",
    )
)
val photoRemote2 = PhotoRemote(
    id = "2",
    urls = Urls(
        raw = "https://example.com/photo2",
        full = "",
        regular = "",
        small = "",
    ),
    location = Location(
        city = "",
        country = ""
    ),
    user = UserData(
        id = "",
        username = "",
        name = "",
    )
)
val photoRemote3 = PhotoRemote(
    id = "3",
    urls = Urls(
        raw = "https://example.com/photo3",
        full = "",
        regular = "",
        small = "",
    ),
    location = Location(
        city = "",
        country = ""
    ),
    user = UserData(
        id = "",
        username = "",
        name = "",
    )
)