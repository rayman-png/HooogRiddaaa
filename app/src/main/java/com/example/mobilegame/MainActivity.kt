package com.example.mobilegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.example.mobilegame.ui.theme.MobileGameTheme


data object Home

@Serializable
data class LogIn(val ID:String, val password:String)

object UserManager {
    private const val FILE_NAME = "users.json"

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val context: Context = this
        @OptIn(ExperimentalMaterial3Api::class)
        setContent {
            MobileGameTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("HooogRiddaaa")
                            }
                        )
                    },
                ) { innerPadding ->
                    NavLogic(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NavLogic(modifier: Modifier = Modifier) {

    // back stack for back buttons
    val backstack = remember{ mutableStateListOf<Any>(Home) }

    // display which screen to show
    NavDisplay(
        backStack = backstack,
        onBack = {
            backstack.removeLastOrNull()
        },
        entryProvider = {key ->
            when(key){
                is Home -> NavEntry(key){
                    HomeScreen(goNext = {
                            id,pass -> backstack.add(LogIn(id,pass))
                    },
                        modifier)
                }
                is LogIn -> NavEntry(key){
                    GameStartScreen(login = key, modifier)
                }

                else -> NavEntry(key){
                    Text(text = "Unknown")
                }

            }
        }
    )
}

@Composable
fun HomeScreen(goNext:(String,String)->Unit, modifier: Modifier = Modifier) {

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
                        goNext(inputID,inputPasswd)
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

@Composable
fun GameStartScreen(login:LogIn, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ){
        Button(
            onClick = {

            }){
            Text("Start")
        }

        Button(
            onClick = {

            }){
            Text("Settings")
        }
        Button(
            onClick = {

            }){
            Text("Scores")
        }

        Button(
            onClick = {
                // Cast the context to an Activity and call finish()
                (context as? android.app.Activity)?.finish()
            }){
            Text("Exit")
        }

    }
}