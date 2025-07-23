package com.example.simplegallery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.simplegallery.ui.PhotoGalleryScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PhotoGalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotoGalleryScreenViewModel = koinViewModel()
) {
    val lazyGridState = rememberLazyGridState()
    val photos = viewModel.photos.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = lazyGridState,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 40.dp),
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        items(photos.itemCount) { index ->
            AsyncImage(
                model = photos[index]?.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        photos.apply {
            when {
                loadState.refresh is androidx.paging.LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
                loadState.append is androidx.paging.LoadState.Error -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text("Error loading more photos", color = Color.Red)
                    }
                }
            }
        }
    }
}