package com.example.simplegallery.network

import com.example.simplegallery.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//Change 1:Use API_KEY from build config
const val API_KEY = "w3RfaqXkhVmuodyyszutfM1dN-hy3v7m2t-9SA3klkc"

interface PhotoService {
    //Change 2: We can move this header to the okHttp class in the retrofit builder
   // @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 50
    ): List<PhotoRemote>
}