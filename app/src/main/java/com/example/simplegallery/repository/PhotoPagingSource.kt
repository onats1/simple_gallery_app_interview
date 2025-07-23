package com.example.simplegallery.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.simplegallery.data.GalleryImage
import com.example.simplegallery.network.PhotoRemote
import com.example.simplegallery.network.PhotoService

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
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}