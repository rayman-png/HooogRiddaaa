package com.example.mobilegame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope // Added this
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch // Added this

@Composable
fun HomeScreen(goNext:(String)->Unit, modifier: Modifier = Modifier) {

    var inputID by remember { mutableStateOf("") }
    var inputPasswd by remember { mutableStateOf("") }
    var errorID by remember { mutableStateOf(-1) }
    val context = LocalContext.current

    // 1. Create a coroutine scope for the network calls
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ){
        TextField(
            value = inputID,
            onValueChange = { inputID = it },
            label = { Text("Username") }
        )

        TextField(
            value = inputPasswd,
            onValueChange = { inputPasswd = it },
            label = { Text("Password") }
        )

        Row {
            Button(
                onClick = {
                    // 2. Wrap network call in a coroutine
                    coroutineScope.launch {
                        val result = UserManager.checkLogin(inputID, inputPasswd)

                        if (result == -1) {
                            android.widget.Toast.makeText(context, "Welcome $inputID!", android.widget.Toast.LENGTH_SHORT).show()
                            goNext(inputID)
                        }
                        errorID = result
                    }
                }){
                Text("Login")
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        val result = UserManager.registerUser(LogIn(inputID, inputPasswd))

                        if (result == -1) {
                            android.widget.Toast.makeText(context, "Account Created!", android.widget.Toast.LENGTH_SHORT).show()
                            // Move to the next screen automatically!
                            goNext(inputID)
                        }
                        errorID = result
                    }
                }){
                Text("Register")
            }
        }

        // Error messages remain the same
        if(errorID == 0) {
            Text("Username Doesn't Exist", color = Color.Red)
        }
        if(errorID == 1) {
            Text("Incorrect Password", color = Color.Red)
        }
        if(errorID == 2) {
            Text("Username Already Exists", color = Color.Red)
        }
// ... existing error messages ...
        if(errorID == 3) {
            Text("System Error: Could not save user", color = Color.Red)
        }
        // ADD THIS:
        if(errorID == 4) {
            Text("Password must be at least 6 characters", color = Color.Red)
        }
    }
}