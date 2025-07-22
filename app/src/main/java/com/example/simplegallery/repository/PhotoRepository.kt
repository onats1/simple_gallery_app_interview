package com.example.simplegallery.repository

import com.example.simplegallery.data.GalleryImage
import com.example.simplegallery.network.PhotoService

class PhotoRepository(private val service: PhotoService) {
    suspend fun getRandomPhotos(): List<GalleryImage> {
        return service.getPhotos().map { remote ->
            GalleryImage(id = remote.id, url = remote.urls.regular)
        }
    }
}