package com.example.mobilegame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

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
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("HooogRiddaaa", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Log in to start hunting", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = inputID,
                    onValueChange = { inputID = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = inputPasswd,
                    onValueChange = { inputPasswd = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                    Button(
                        onClick = {
                            val result = UserManager.checkLogin(context, inputID, inputPasswd)
                            if (result == -1) {
                                android.widget.Toast.makeText(context, "Welcome $inputID!", android.widget.Toast.LENGTH_SHORT).show()
                                goNext(inputID)
                            }

                            errorID = result

                        },
                        modifier = Modifier.weight(1f)
                    ){
                        Text("Login")
                    }

                    OutlinedButton(
                        onClick = {
                            val result = UserManager.registerUser(context, LogIn(inputID, inputPasswd))

                            if (result == -1) {
                                android.widget.Toast.makeText(context, "Account Created!", android.widget.Toast.LENGTH_SHORT).show()
                                inputID = ""
                                inputPasswd = ""
                            }

                            errorID = result
                        },
                        modifier = Modifier.weight(1f)
                    ){
                        Text("Register")
                    }
                }
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