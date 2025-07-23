package com.example.simplegallery.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.simplegallery.data.GalleryImage
import com.example.simplegallery.domain.Resource
import com.example.simplegallery.network.PhotoService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoRepository(private val service: PhotoService) {
    fun getRandomPhotos(): Flow<PagingData<GalleryImage>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PhotoPagingSource(service) }
        ).flow
            .map { remotePagingData ->
                remotePagingData.map { remotePhoto ->
                    GalleryImage(id = remotePhoto.id, url = remotePhoto.urls.regular)
                }
            }
    }
}