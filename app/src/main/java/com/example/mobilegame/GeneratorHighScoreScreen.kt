package com.example.mobilegame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GeneratorHighScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigationToDisplay: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { viewModel.insertRandomScore() }) {
            Text("Generate Random Score")
        }

        Button(onClick = { onNavigationToDisplay() }) {
            Text("View Leaderboard")
        }
    }
}