package com.example.simplegallery.domain.usecases

import com.example.simplegallery.domain.repo.PhotoRepository

class GetPhotosUseCase(private val repository: PhotoRepository) {
    operator fun invoke() = repository.getRandomPhotos()
}