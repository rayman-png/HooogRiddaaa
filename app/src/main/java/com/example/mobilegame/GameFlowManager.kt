package com.example.mobilegame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LevelTransitionScreen(
    nextCategory: String,
    allCategories: List<String>,
    onTransitionFinished: () -> Unit
) {
    var displayedCategory by remember { mutableStateOf(allCategories.random()) }

    LaunchedEffect(nextCategory) {
        for (i in 1..20) {
            displayedCategory = allCategories.random()
            delay(40L + (i * 10L))
        }
        displayedCategory = nextCategory
        delay(2000L)
        onTransitionFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Level Clear! 🎉",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Rolling next category...",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(100.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayedCategory,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun GameFlowManager(
    username: String,
    gameMode: String,
    onGameFinished: () -> Unit
) {
    var currentLevel by remember { mutableStateOf(0) }
    var currentCategory by remember { mutableStateOf("") }
    var playedCategories by remember { mutableStateOf(setOf<String>()) }

    var isTransitioning by remember { mutableStateOf(false) }
    var upcomingCategory by remember { mutableStateOf("") }

    val allCategories = listOf("Nature", "Kitchen", "Office", "Pets")

    if (isTransitioning) {
        LevelTransitionScreen(
            nextCategory = upcomingCategory,
            allCategories = allCategories,
            onTransitionFinished = {
                currentCategory = upcomingCategory
                playedCategories = playedCategories + upcomingCategory
                currentLevel++
                isTransitioning = false
            }
        )
    } else {
        when (currentLevel) {
            0 -> {
                CategoryScreen(
                    onCategorySelected = { selected ->
                        currentCategory = selected
                        playedCategories = setOf(selected)
                        currentLevel = 1
                    }
                )
            }
            1, 2, 3 -> {
                // Determine which screen to show based on gameMode
                if (gameMode == "Category Shuffle") {
                    CategoryShuffleScreen(
                        username = username,
                        category = currentCategory,
                        level = currentLevel,
                        onLevelComplete = {
                            if (currentLevel == 3) {
                                onGameFinished()
                            } else {
                                val remainingCategories = allCategories - playedCategories
                                upcomingCategory = remainingCategories.random()
                                isTransitioning = true
                            }
                        }
                    )
                } else {
                    // This handles the "Mystery Hunt" choice
                    MysteryHuntScreen(
                        username = username,
                        category = currentCategory,
                        level = currentLevel,
                        onLevelComplete = {
                            if (currentLevel == 3) {
                                onGameFinished()
                            } else {
                                currentLevel++
                            }
                        }
                    )
                }
            }
        }
    }
}
