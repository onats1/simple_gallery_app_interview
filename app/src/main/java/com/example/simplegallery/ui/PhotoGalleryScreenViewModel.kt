package com.example.simplegallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.simplegallery.data.GalleryImage
import com.example.simplegallery.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow

class PhotoGalleryScreenViewModel(
    private val repository: PhotoRepository
): ViewModel() {

    val photos: Flow<PagingData<GalleryImage>> = repository.getRandomPhotos()
        .cachedIn(viewModelScope)

}