package com.example.mobilegame

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

fun analyzePhoto(
    context: Context,
    uri: Uri,
    target: String,
    onResult: (Boolean, Float) -> Unit
) {
    val image = InputImage.fromFilePath(context, uri)
    val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    labeler.process(image)
        .addOnSuccessListener { labels ->
            val match = labels.find {
                it.text.contains(target, ignoreCase = true) && it.confidence >= 0.8f
            }
            if (match != null) {
                onResult(true, match.confidence)
            } else {
                onResult(false, 0f)
            }
        }
}