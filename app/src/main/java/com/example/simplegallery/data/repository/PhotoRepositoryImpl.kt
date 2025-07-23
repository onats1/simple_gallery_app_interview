package com.example.simplegallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.simplegallery.domain.models.GalleryImage
import com.example.simplegallery.domain.repo.PhotoRepository
import com.example.simplegallery.data.remote.services.PhotoService
import com.example.simplegallery.data.remote.utils.toGalleryImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoRepositoryImpl(private val service: PhotoService): PhotoRepository {
    override fun getRandomPhotos(): Flow<PagingData<GalleryImage>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PhotoPagingSource(service) }
        ).flow
            .map { remotePagingData ->
                remotePagingData.map { remotePhoto ->
                    remotePhoto.toGalleryImage()
                }
            }
    }
}