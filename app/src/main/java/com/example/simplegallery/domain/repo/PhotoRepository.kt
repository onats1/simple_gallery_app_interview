package com.example.simplegallery.domain.repo

import androidx.paging.PagingData
import com.example.simplegallery.domain.models.GalleryImage
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getRandomPhotos(): Flow<PagingData<GalleryImage>>
}