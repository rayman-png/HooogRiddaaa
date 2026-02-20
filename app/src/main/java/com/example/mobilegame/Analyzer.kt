package com.example.mobilegame

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

fun analyzePhoto(
    context: Context,
    uri: Uri,
    validTargets: List<String>,
    // onResult returns: (isFound, confidence, topDetectedLabel)
    onResult: (Boolean, Float, String?) -> Unit
) {
    try {
        val image = InputImage.fromFilePath(context, uri)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                // 1. Look for a match among our valid synonyms
                val match = labels.find { detectedLabel ->
                    validTargets.any { validTarget ->
                        detectedLabel.text.equals(validTarget, ignoreCase = true)
                    } && detectedLabel.confidence >= 0.7f
                }

                if (match != null) {
                    onResult(true, match.confidence, match.text)
                } else {
                    // 2. If no match, find the most confident label for cheeky feedback
                    val topLabel = labels.maxByOrNull { it.confidence }?.text
                    onResult(false, 0f, topLabel)
                }
            }
            .addOnFailureListener {
                onResult(false, 0f, "Error: ${it.message}")
            }
    } catch (e: Exception) {
        onResult(false, 0f, "Error: ${e.message}")
    }
}