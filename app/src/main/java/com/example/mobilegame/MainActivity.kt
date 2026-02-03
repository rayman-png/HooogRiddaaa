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
data class MainMenu(val username: String)
@Serializable
data class GameHome(val username: String)
@Serializable
data class GameSession(val username: String, val category: String)
@Serializable
data class LogIn(val ID:String, val password:String)

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
                            id -> backstack.add(MainMenu(id))
                    },
                        modifier)
                }
                is MainMenu -> NavEntry(key) {
                    MainMenuScreen(
                        username = key.username,
                        onGameMenu = { backstack.add(GameHome(key.username)) },
                        onLogout = { backstack.removeLastOrNull()}
                    )
                }
                is GameHome ->NavEntry(key){
                    CategoryScreen(
                        onCategorySelected = { category ->
                            backstack.add(GameSession(key.username, category))
                        }
                    )
                }
                is GameSession -> NavEntry(key) {
                    GameScreen(username = key.username, category = key.category)
                }

                else -> NavEntry(key){
                    Text(text = "Unknown")
                }

            }
        }
    )
}