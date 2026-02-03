package com.example.mobilegame

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@Composable
fun GameScreen(username: String,category: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // should be getting one from a list based on category
    val targetObject = "Coffee mug"

    var winMessage by remember { mutableStateOf("Find a $targetObject!") }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && capturedImageUri != null) {
            analyzePhoto(context, capturedImageUri!!, targetObject) { found, confidence ->
                winMessage = if (found) "You Win! ($confidence)" else "Try again!"
            }
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(winMessage)

        IconButton(
            onClick = {
                val uri = createTempImageUri(context)
                capturedImageUri = uri
                cameraLauncher.launch(uri)
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Filled.CameraAlt, contentDescription = "Camera")
        }
    }
}


