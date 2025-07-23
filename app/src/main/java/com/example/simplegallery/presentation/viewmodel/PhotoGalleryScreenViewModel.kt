package com.example.simplegallery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.simplegallery.domain.models.GalleryImage
import com.example.simplegallery.domain.usecases.GetPhotosUseCase
import kotlinx.coroutines.flow.Flow

class PhotoGalleryScreenViewModel(
     getPhotosUseCase: GetPhotosUseCase,
): ViewModel() {

    val photos: Flow<PagingData<GalleryImage>> = getPhotosUseCase()
        .cachedIn(viewModelScope)

}