package com.example.simplegallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.simplegallery.data.GalleryImage
import com.example.simplegallery.repository.PhotoRepository
import com.example.simplegallery.ui.theme.SimpleGalleryTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleGalleryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PhotoGalleryScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoGalleryScreen(modifier: Modifier = Modifier) {
    val repo: PhotoRepository = koinInject()
    val lazyGridState = rememberLazyGridState()
    val photos = remember { mutableStateListOf<GalleryImage>() }

    LaunchedEffect(Unit) {
        photos.addAll(repo.getRandomPhotos())
    }

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
        items(items = photos.toList()) { image ->
            AsyncImage(
                model = image.url,
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleGalleryTheme {
        PhotoGalleryScreen()
    }
}