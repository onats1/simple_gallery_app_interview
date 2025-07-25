package com.example.simplegallery.data.remote.services

import com.example.simplegallery.data.remote.models.PhotoRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {
    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<PhotoRemote>>
}