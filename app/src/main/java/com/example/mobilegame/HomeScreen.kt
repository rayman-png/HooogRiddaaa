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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeScreen(goNext:(String)->Unit, modifier: Modifier = Modifier) {

    // save input nric and bool for error
    var inputID by remember { mutableStateOf("") }
    var inputPasswd by remember { mutableStateOf("") }
    var errorID by remember { mutableStateOf(-1) }
    val context = LocalContext.current // This gets the current Context


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ){
        // save input as nric
        TextField(
            value = inputID,
            onValueChange = {
                inputID = it
            },
            label = {
                Text("Username")
            }
        )

        TextField(
            value = inputPasswd,
            onValueChange = {
                inputPasswd = it
            },
            label = {
                Text("Password")
            }
        )

        Row(){

            Button(
                onClick = {
                    val result = UserManager.checkLogin(context, inputID, inputPasswd)
                    // if matches then go to next screen
                    if (result == -1) {
                        android.widget.Toast.makeText(context, "Welcome $inputID!", android.widget.Toast.LENGTH_SHORT).show()
                        goNext(inputID)
                    }

                    errorID = result

                }){
                Text("Login")
            }

            // change the input to empty string to clear
            Button(
                onClick = {
                    val result = UserManager.registerUser(context, LogIn(inputID, inputPasswd))

                    if (result == -1) {
                        android.widget.Toast.makeText(context, "Account Created!", android.widget.Toast.LENGTH_SHORT).show()
                        inputID = ""
                        inputPasswd = ""
                    }

                    errorID = result
                }){
                Text("Register")
            }
        }
        // if error then show error message
        if(errorID == 0) {
            Text("Username Doesn't Exist", color = Color.Red)
        }
        if(errorID == 1) {
            Text("Incorrect Password", color = Color.Red)
        }
        if(errorID == 2) {
            Text("Username Already Exists", color = Color.Red)
        }
        if(errorID == 3) {
            Text("System Error: Could not save user", color = Color.Red)
        }
    }
}