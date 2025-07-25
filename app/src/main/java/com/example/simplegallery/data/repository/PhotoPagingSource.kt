package com.example.simplegallery.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.simplegallery.data.remote.models.PhotoRemote
import com.example.simplegallery.data.remote.services.PhotoService

class PhotoPagingSource(
    private val photoService: PhotoService
): PagingSource<Int, PhotoRemote>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoRemote>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoRemote> {
        return try {
            val page = params.key ?: 1
            val response = photoService.getPhotos(page = page, perPage = params.loadSize)
            if (response.isSuccessful) {
                val data = response.body() ?: listOf()
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load photos"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}