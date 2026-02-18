package com.example.mobilegame

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

fun analyzePhoto(
    context: Context,
    uri: Uri,
    target: String,
    onResult: (Boolean, Float, List<String>) -> Unit
) {
    try {
        val image = InputImage.fromFilePath(context, uri)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                val detectedLabels = labels.map { "${it.text} (${(it.confidence * 100).toInt()}%)" }
                val match = labels.find {
                    it.text.contains(target, ignoreCase = true) && it.confidence >= 0.7f
                }
                if (match != null) {
                    onResult(true, match.confidence, detectedLabels)
                } else {
                    onResult(false, 0f, detectedLabels)
                }
            }
            .addOnFailureListener {
                onResult(false, 0f, listOf("Error: ${it.message}"))
            }
    } catch (e: Exception) {
        onResult(false, 0f, listOf("Error: ${e.message}"))
    }
}