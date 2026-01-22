package com.example.mobilegame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun GameScreen(username: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // This is where the Camera Preview will go
        Text("Camera Feed Loading for $username...")

        // You will overlay your ML results (e.g., "Found a Mug!") here
    }
}