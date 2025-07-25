package com.example.simplegallery.data.remote.utils

import com.example.simplegallery.data.remote.models.PhotoRemote
import com.example.simplegallery.domain.models.GalleryImage

fun PhotoRemote.toGalleryImage() = GalleryImage(id = this.id, url = this.urls.regular)