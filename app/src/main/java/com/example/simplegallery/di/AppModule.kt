package com.example.simplegallery.di

import com.example.simplegallery.BuildConfig
import com.example.simplegallery.repository.PhotoRepository
import com.example.simplegallery.network.PhotoService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    //Change: Add OkHttp as a dependency
    single { okHttp }
    single { provideRetrofit(get()) }
    single { createImageService(get()) }
    single { PhotoRepository(get()) }
}

//Change: Add OkHttp interceptor as a dependency
private val okHttp = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Client-ID ${BuildConfig.API_KEY}")
            .build()
        chain.proceed(request)
    }
    .build()

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.unsplash.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()



private fun createImageService(retrofit: Retrofit) = retrofit.create(PhotoService::class.java)