package com.example.mobilegame

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File



object ScoreManager {
    private const val FILE_NAME = "scores.json"

    private fun getFile(context: Context): File {
        val file = File(context.filesDir, FILE_NAME)


        if (!file.exists()) {
            file.createNewFile()

            file.writeText("[]")
        }
        return file
    }


    fun registerUser(context: Context, newUser: LogIn): Int {
        if(newUser.ID == "" || newUser.password == "")
            return 3

        return try {
            val file = getFile(context)
            val jsonString = file.readText()


            val userList = if (jsonString.isBlank() || jsonString == "null") {
                mutableListOf<LogIn>()
            } else {
                Json.decodeFromString<List<LogIn>>(jsonString).toMutableList()
            }


            if (userList.any { it.ID == newUser.ID }) return 2


            userList.add(newUser)
            file.writeText(Json.encodeToString(userList))
            -1
        } catch (e: Exception) {
            e.printStackTrace()
            3
        }
    }


    fun checkLogin(context: Context, id: String, pass: String): Int {
        val file = getFile(context)


        if (!file.exists()) return 0

        val userList = Json.decodeFromString<List<LogIn>>(file.readText())


        val user = userList.find { it.ID == id }

        return when {
            user == null -> 0
            user.password != pass -> 1
            else -> -1
        }
    }
}