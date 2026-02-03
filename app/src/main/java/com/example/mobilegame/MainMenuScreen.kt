package com.example.mobilegame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuScreen(
    username: String,
    onGameMenu: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, $username To the Scavenger Hunt!", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onGameMenu,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Start Game")
        }

        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth(0.7f)){
            Text("Scores")
        }

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Logout")
        }
    }
}