package com.example.simplegallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplegallery.presentation.ui.screens.PhotoGalleryScreen
import com.example.simplegallery.presentation.ui.theme.SimpleGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SimpleGalleryTheme {
                NavHost(navController, startDestination = "gallery") {
                    composable("gallery") {
                        PhotoGalleryScreen()
                    }
                }
            }
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