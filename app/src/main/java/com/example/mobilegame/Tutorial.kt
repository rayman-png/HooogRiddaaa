package com.example.mobilegame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "HOW TO PLAY",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            TutorialCard(title = "Welcome, $username!") {
                Text(
                    text = "Start the game and Choose your Categories to Hunt! A timer will be counting the moment you start, so get as many points as you can!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            TutorialCard(title = "Mystery Hunt") {
                Text(
                    text = "Hints will be given for the object you need to find. Solve the mystery and take a picture!",
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            TutorialCard(title = "Category Shuffle") {
                Text(
                    text = "Choose your starting category and find the correct object! Once you get it right, the next Category will be randomized.",
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            TutorialCard(title = "High Scores") {
                Text(
                    text = "After the timer is up, check the leaderboard and see who is the best!",
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            TutorialCard(title = "Controls") {
                Column {
                    ControlRow(icon = Icons.Default.AddAPhoto, description = "Take a Picture")
                    ControlRow(icon = Icons.Default.AddToPhotos, description = "Choose from Gallery")
                    ControlRow(icon = Icons.Default.ArrowForward, description = "Skip (Will deduct points)")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("All Set!")
            }
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

@Composable
fun TutorialCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
