import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.simplegallery.data.remote.models.PhotoRemote
import com.example.simplegallery.data.remote.services.PhotoService
import com.example.simplegallery.data.remote.utils.toGalleryImage
import com.example.simplegallery.data.repository.PhotoPagingSource
import com.example.simplegallery.data.repository.PhotoRepositoryImpl
import com.example.simplegallery.domain.models.GalleryImage
import com.example.simplegallery.domain.repo.PhotoRepository
import com.example.simplegallery.utils.photoRemote1
import com.example.simplegallery.utils.photoRemote2
import com.example.simplegallery.utils.photoRemote3
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkConstructor
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import retrofit2.Response

@ExperimentalCoroutinesApi
class PhotoRepositoryTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()


    private val remotePhotos = listOf(photoRemote1, photoRemote2, photoRemote3)
    private val expectedGalleryImages = remotePhotos.map { it.toGalleryImage() }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        startKoin {
            modules(
                module {
                    single { mockk<PhotoService>() }
                    single<PhotoRepository> { PhotoRepositoryImpl(get()) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        stopKoin()
    }

    @Test
    fun `getRandomPhotos emits PagingData with mapped GalleryImages`() = runTest {
        // Given
        val service: PhotoService = get()
        coEvery { service.getPhotos(1, 10) } returns Response.success(remotePhotos)

        val repository: PhotoRepository by inject()
        val flow = repository.getRandomPhotos()


        // Collect the flow using AsyncPagingDataDiffer
        val differ = AsyncPagingDataDiffer(
            diffCallback = GalleryImageDiffCallback(),
            updateCallback = NoopListCallback(),
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher
        )

        val job = launch {
            flow.collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        runCurrent()

        // Then
        val actual = differ.snapshot().items
        assertEquals(expectedGalleryImages, actual)

        job.cancel()
    }

    private class NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) = Unit
        override fun onRemoved(position: Int, count: Int) = Unit
        override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
        override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
    }

    private class GalleryImageDiffCallback : DiffUtil.ItemCallback<GalleryImage>() {
        override fun areItemsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean =
            oldItem == newItem
    }
}
