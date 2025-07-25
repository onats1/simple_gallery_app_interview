package com.example.simplegallery.di

import com.example.simplegallery.BuildConfig
import com.example.simplegallery.data.repository.PhotoRepositoryImpl
import com.example.simplegallery.data.remote.services.PhotoService
import com.example.simplegallery.domain.repo.PhotoRepository
import com.example.simplegallery.domain.usecases.GetPhotosUseCase
import com.example.simplegallery.presentation.viewmodel.PhotoGalleryScreenViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    //Change: Add OkHttp as a dependency
    single { okHttp }
    single { provideRetrofit(get()) }
    single { createImageService(get()) }
    single<PhotoRepository> { PhotoRepositoryImpl(get()) }
    single { GetPhotosUseCase(get()) }
    viewModel { PhotoGalleryScreenViewModel(get()) }
}

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

//Change: Add OkHttp interceptor as a dependency
private val okHttp = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
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