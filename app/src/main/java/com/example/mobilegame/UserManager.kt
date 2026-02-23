package com.example.mobilegame


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
object UserManager {
    // 1. Get the Firebase instance
    private val auth = FirebaseAuth.getInstance()

    // 2. Helper to turn a "Username" into an "Email" for Firebase
    private fun formatEmail(id: String): String {
        return if (id.contains("@")) id else "$id@scavengerhunt.com"
    }

    // 3. Use 'suspend' because network calls take time
    suspend fun registerUser(newUser: LogIn): Int {
        if (newUser.ID.isBlank() || newUser.password.isBlank()) return 3

        val email = formatEmail(newUser.ID)

        return try {
            auth.createUserWithEmailAndPassword(email, newUser.password).await()
            -1 // Success
        } catch (e: FirebaseAuthUserCollisionException) {
            2 // Username already exists
        } catch (e: FirebaseAuthWeakPasswordException) {
            4 // Password is less than 6 characters
        } catch (e: Exception) {
            e.printStackTrace()
            3 // System Error
        }
    }

    // 4. Use 'suspend' for logging in
    suspend fun checkLogin(id: String, pass: String): Int {
        if (id.isBlank() || pass.isBlank()) return 0

        val email = formatEmail(id)

        return try {
            // Tell Firebase to sign in
            auth.signInWithEmailAndPassword(email, pass).await()
            -1 // Success
        } catch (e: FirebaseAuthInvalidUserException) {
            0 // Username doesn't exist
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            1 // Incorrect password
        } catch (e: Exception) {
            e.printStackTrace()
            3 // System error
        }
    }
}