package com.example.mobilegame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun GameScreen(username: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text("Main Game window plus machine learning thingy")

        IconButton(
            onClick = {
                // Aloy open camera here
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // This does the magic
                .padding(16.dp)            // Adds space from the edges
        ) {
            Icon(Icons.Filled.CameraAlt, contentDescription = "Camera")
        }
    }
}
