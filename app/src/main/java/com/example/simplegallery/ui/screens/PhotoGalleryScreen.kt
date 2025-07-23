package com.example.simplegallery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.simplegallery.ui.PhotoGalleryScreenViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
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
        when (val refreshState = photos.loadState.refresh) {
            is LoadState.Loading -> {
                items(10) {
                    ShimmerPhotoItem()
                }
            }

            is LoadState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(refreshState.error.message ?: "Unknown error", color = Color.Red)
                }
            }

            else -> {
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
            }
        }
    }
}

@Composable
fun ShimmerPhotoItem() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(0.7f)
        .clip(RoundedCornerShape(12.dp))
        .placeholder(
            visible = true,
            highlight = PlaceholderHighlight.shimmer(),
            color = Color.White,
            shape = RoundedCornerShape(12.dp)
        ))
}