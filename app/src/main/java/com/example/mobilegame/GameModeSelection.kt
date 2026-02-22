package com.example.mobilegame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameModeSelectionScreen(
    onModeSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Mode", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        //
        Button(
            onClick = { onModeSelected("Mystery Hunt") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Mystery Hunt")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // NEW: Second Mode
        Button(
            onClick = { onModeSelected("Category Shuffle") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Category Shuffle")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Back")
        }
    }
}