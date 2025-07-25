package com.example.simplegallery.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.simplegallery.domain.models.GalleryImage
import com.example.simplegallery.domain.usecases.GetPhotosUseCase
import com.example.simplegallery.presentation.viewmodel.PhotoGalleryScreenViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoGalleryScreenViewModelTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeImages = listOf(
        GalleryImage(id = "1", url = "url1"),
        GalleryImage(id = "2", url = "url2")
    )

    private val fakeGetPhotosUseCase = mockk<GetPhotosUseCase>()

    private val testModule = module {
        single<GetPhotosUseCase> { fakeGetPhotosUseCase }
        viewModel { PhotoGalleryScreenViewModel(get()) }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { fakeGetPhotosUseCase.invoke() } returns flowOf(PagingData.from(fakeImages))

        startKoin {
            modules(testModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `photos emits expected PagingData`() = runTest {
        val viewModel: PhotoGalleryScreenViewModel = getKoin().get()

        val differ = AsyncPagingDataDiffer<GalleryImage>(
            diffCallback = object : DiffUtil.ItemCallback<GalleryImage>() {
                override fun areItemsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
                    return oldItem == newItem
                }
            },
            updateCallback = NoopListCallback(),
            mainDispatcher = testDispatcher
        )

        val job = launch {
            viewModel.photos.collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        // Advance until data is loaded
        advanceUntilIdle()

        // Now get the snapshot and compare
        val images = differ.snapshot().items

        assertEquals(fakeImages, images)

        job.cancel()
    }

    private class NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}
