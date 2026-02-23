package com.example.mobilegame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuScreen(
    username: String,
    onGameMenu: () -> Unit,
    onLogout: () -> Unit,
    onTutorial: () -> Unit,
    onShowScores: () -> Unit, // Added this
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome back, $username", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = "Ready for your next hunt?", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onGameMenu,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Start Game")
        }

        Button(
            onClick = onShowScores, // Updated this
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Scores")
        }

        Button(
            onClick = onTutorial, // Updated this
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("How To Play")
        }

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Logout")
        }
    }
}