package com.example.mobilegame

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createTempImageUri(context: Context): Uri {
    val tempFile = File.createTempFile("scavenger_capture", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Must match your Manifest
        tempFile
    )
}