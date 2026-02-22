package com.example.mobilegame

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

fun analyzePhoto(
    context: Context,
    uri: Uri,
    target: ScavengerTarget,
    onResult: (Boolean, Float, String?) -> Unit
) {
    try {
        val image = InputImage.fromFilePath(context, uri)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                // --- DEBUG PRINTING ---
                println("MLKit_Debug: --- Analysis Started ---")
                if (labels.isEmpty()) {
                    println("MLKit_Debug: No objects detected at all.")
                } else {
                    labels.forEach { label ->
                        // This prints every object found and its confidence (e.g., 0.85)
                        println("MLKit_Debug: Saw '${label.text}' with confidence: ${label.confidence}")
                    }
                }
                println("MLKit_Debug: Target Threshold is: ${target.threshold}")

                // 1. Look for a match among our valid synonyms
                val match = labels.find { detectedLabel ->
                    target.synonyms.any { synonym ->
                        synonym.equals(detectedLabel.text, ignoreCase = true)
                    } && detectedLabel.confidence >= target.threshold
                }

                if (match != null) {
                    println("MLKit_Debug: MATCH FOUND! '${match.text}' at ${match.confidence}")
                    onResult(true, match.confidence, match.text)
                } else {
                    val topLabel = labels.maxByOrNull { it.confidence }?.text
                    println("MLKit_Debug: NO MATCH. Top detected was: $topLabel")
                    onResult(false, 0f, topLabel)
                }
            }
            .addOnFailureListener {
                println("MLKit_Debug: Error: ${it.message}")
                onResult(false, 0f, "Error: ${it.message}")
            }
    } catch (e: Exception) {
        println("MLKit_Debug: Exception: ${e.message}")
        onResult(false, 0f, "Error: ${e.message}")
    }
}