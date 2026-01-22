package com.example.mobilegame

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


// just a copy of user manager for now but edit to store scores
object ScoreManager {
    private const val FILE_NAME = "scores.json"

    // Helper to get the file reference
    private fun getFile(context: Context): File {
        val file = File(context.filesDir, FILE_NAME)

        // CREATE IF IT DOESN'T EXIST
        if (!file.exists()) {
            file.createNewFile()
            // Initialize with an empty JSON array so the parser doesn't crash
            file.writeText("[]")
        }
        return file
    }

    // 1. Register a new user
    fun registerUser(context: Context, newUser: LogIn): Int {
        if(newUser.ID == "" || newUser.password == "")
            return 3

        return try {
            val file = getFile(context)
            val jsonString = file.readText()

            // Use a standard List first, then convert to Mutable
            val userList = if (jsonString.isBlank() || jsonString == "null") {
                mutableListOf<LogIn>()
            } else {
                Json.decodeFromString<List<LogIn>>(jsonString).toMutableList()
            }

            // Check if user already exists
            if (userList.any { it.ID == newUser.ID }) return 2

            // Add and save
            userList.add(newUser)
            file.writeText(Json.encodeToString(userList))
            -1 // Success
        } catch (e: Exception) {
            e.printStackTrace()
            3 // Return a new error code for "File/System Error"
        }
    }

    // 2. Updated Login check (reads from Internal Storage)
    fun checkLogin(context: Context, id: String, pass: String): Int {
        val file = getFile(context)

        // If no users exist at all, the ID definitely isn't there
        if (!file.exists()) return 0

        val userList = Json.decodeFromString<List<LogIn>>(file.readText())

        // 1. Find if the user ID exists
        val user = userList.find { it.ID == id }

        return when {
            user == null -> 0          // ID doesn't exist
            user.password != pass -> 1 // ID exists, but password is wrong
            else -> -1                 // Everything matches (Success)
        }
    }
}