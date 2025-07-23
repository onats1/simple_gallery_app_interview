package com.example.simplegallery.repository

import com.example.simplegallery.data.GalleryImage
import com.example.simplegallery.network.PhotoService

class PhotoRepository(private val service: PhotoService) {
    suspend fun getRandomPhotos(page: Int, perPage: Int = 50): List<GalleryImage> {
        return service.getPhotos(page = 1, perPage = 10).map { remote ->
            GalleryImage(id = remote.id, url = remote.urls.regular)
        }
    }
}