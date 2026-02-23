package com.example.mobilegame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TutorialScreen(
    username: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "HOW TO PLAY",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section: The Objective
        Text(text = "Welcome To the Scavenger Hunt", fontWeight = FontWeight.Bold)
        Text(
            text = "Start the game and Choose your Categories to Hunt! A timer will be counting the moment you start, so get as many points as you can!",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Mystery Hunt", fontWeight = FontWeight.Bold)
        Text(
            text = "Hints will be given for the object you need to find, Solve the Mystery and take a picture!",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Category Shuffle", fontWeight = FontWeight.Bold)
        Text(
            text = "Choose your starting category and find the correct object! Once you get it right, the next Category will be randomized, all the best!",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Controls
        Text(text = "CONTROLS", fontWeight = FontWeight.Bold)
        ControlRow(icon = Icons.Default.AddAPhoto, description = "Take a Picture with your Camera")
        ControlRow(icon = Icons.Default.AddToPhotos, description = "Choose a Picture from Gallery")
        ControlRow(icon = Icons.Default.ArrowForward, description = "Skip the current object")
        Text(
            text = "(Skipping the current object will consume points!)",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // Back Button
        Button(onClick = onBack) {
            Text("All Set!")
        }
    }
}

@Composable
fun ControlRow(icon: ImageVector, description: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null)
        Text(text = description, modifier = Modifier.padding(8.dp))
    }
}
