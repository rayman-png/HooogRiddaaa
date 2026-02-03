package com.example.mobilegame

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

@Composable
fun OpenCamera() {
    //val context = LocalContext.current
    //var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    //val cameraLauncher = rememberLauncherForActivityResult(
     //   contract = ActivityResultContracts.TakePicture()
    //) { success ->
      //  if (success && capturedImageUri != null) {
            // Step 1: Run analysis once photo is saved
      //      analyzePhoto(context, capturedImageUri!!, targetObject)
        //}
    //}

}
