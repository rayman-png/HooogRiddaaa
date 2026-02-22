package com.example.mobilegame

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun CategoryShuffleScreen(
    username: String,
    category: String,
    level: Int,
    onLevelComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val targets = GameLibrary.categoryTargets[category] ?: GameLibrary.categoryTargets["Office"]!!
    val targetKeys = targets.keys.toList()
    var targetObject by remember(category) { mutableStateOf(targetKeys.random()) }

    var statusMessage by remember(category) { mutableStateOf("Find a $targetObject!") }
    var isProcessing by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(showConfetti) {
        if (showConfetti) {
            delay(3000L) // Let the user enjoy the confetti for 3 seconds
            showConfetti = false
            onLevelComplete() // Tell the parent controller we beat the level!
        }
    }

    val processImage: (Uri) -> Unit = { uri ->
        statusMessage = "Analyzing..."
        isProcessing = true
        showConfetti = false

        val currentDataTarget = GameLibrary.categoryTargets[category]?.get(targetObject)

        if (currentDataTarget != null) {
            analyzePhoto(context, uri, currentDataTarget) { found, confidence, topLabel ->
                isProcessing = false
                if (found) {
                    statusMessage = "Success! You found the $targetObject! 🎉"
                    showConfetti = true
                } else {
                    statusMessage = if (topLabel != null && !topLabel.startsWith("Error")) {
                        "Hmm... that looks like a $topLabel, not a $targetObject. Try again!"
                    } else {
                        "I couldn't recognize anything clearly. Try again!"
                    }
                }
            }
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && capturedImageUri != null) {
            processImage(capturedImageUri!!)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { processImage(it) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // NEW: Added Level Indicator
            Text(
                text = "Level $level of 3",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = "Scavenger Hunt",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Category: $category",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Target:", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = targetObject,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = statusMessage,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    if (isProcessing) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    val uri = createTempImageUri(context)
                    capturedImageUri = uri
                    cameraLauncher.launch(uri)
                }) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Camera")
                }

                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Gallery")
                }

                IconButton(onClick = {
                    targetObject = targetKeys.random()
                    statusMessage = "Find a $targetObject!"
                    showConfetti = false
                }) {
                    Icon(Icons.Filled.Refresh, contentDescription = "New Target", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        if (showConfetti) {
            val party = Party(
                speed = 0f, maxSpeed = 30f, damping = 0.9f, spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                position = Position.Relative(0.5, 0.2),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
            )
            KonfettiView(modifier = Modifier.fillMaxSize(), parties = listOf(party))
        }
    }
}

private var persistedUsedTargets = mutableSetOf<String>()
private var persistedWonTargets = mutableSetOf<String>()
private var persistedCategory = ""

@Composable
fun MysteryHuntScreen(
    username: String,
    category: String,
    level: Int,
    onLevelComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Reset if category changed
    if (persistedCategory != category) {
        persistedCategory = category
        persistedUsedTargets = mutableSetOf()
        persistedWonTargets = mutableSetOf()
    }

    val targets = GameLibrary.categoryTargets[category] ?: GameLibrary.categoryTargets["Office"]!!
    val targetKeys = targets.keys.toList()

    var targetObject by remember(category, level) {
        val remaining = targetKeys.filter { it !in persistedUsedTargets && it !in persistedWonTargets }
        val pick = if (remaining.isNotEmpty()) {
            remaining.random()
        } else {
            // Reset used but never bring back won items
            persistedUsedTargets = mutableSetOf()
            val neverWon = targetKeys.filter { it !in persistedWonTargets }
            (if (neverWon.isNotEmpty()) neverWon else targetKeys).random()
        }
        persistedUsedTargets.add(pick)
        mutableStateOf(pick)
    }

    val targetData by remember(targetObject) {
        mutableStateOf(GameLibrary.categoryTargets[category]?.get(targetObject))
    }

    // --- State Management ---
    var showSecondHint by remember(category, level) { mutableStateOf(false) }
    var statusMessage by remember(category, level) { mutableStateOf("HAPPY HUNTING") }
    var isProcessing by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showConfetti by remember { mutableStateOf(false) }

    // --- Level Transition Logic ---
    LaunchedEffect(showConfetti) {
        if (showConfetti) {
            delay(3000L)
            showConfetti = false
            onLevelComplete()
        }
    }

    // --- Image Analysis Execution ---
    val processImage: (Uri) -> Unit = { uri ->
        statusMessage = "Analyzing your find..."
        isProcessing = true
        showConfetti = false

        val currentData = targetData
        if (currentData != null)
            analyzePhoto(context, uri, currentData) { found, confidence, topLabel ->
                isProcessing = false
                if (found) {
                    persistedUsedTargets.add(targetObject)
                    persistedWonTargets.add(targetObject)
                    statusMessage = "Correct! It was a $targetObject!"
                    showConfetti = true
                } else {
                    statusMessage = if (topLabel != null && !topLabel.startsWith("Error")) {
                        "Not quite! That looks like a $topLabel. Keep looking!"
                    } else {
                        "I couldn't see that clearly. Try a different angle!"
                    }
                }
            }
    }

    // --- Launchers ---
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && capturedImageUri != null) {
            processImage(capturedImageUri!!)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { processImage(it) }
    }

    // --- UI Layout ---
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Level Info
            Text(
                text = "Level $level of 3",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Mystery Hunt",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Category: $category",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "PRIMARY CLUE", style = MaterialTheme.typography.labelMedium)
            Text(
                text = targetData?.hint1Vague ?: "No clue found for this item.",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Click to Reveal Hint 2 ---
            if (!showSecondHint) {
                OutlinedButton(
                    onClick = { showSecondHint = true },
                    shape = androidx.compose.foundation.shape.CircleShape
                ) {
                    Text("Need an extra hint?")
                }
            } else {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "DETAILED HINT: ${targetData?.hint2Direct}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Status Messaging ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = statusMessage,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    if (isProcessing) {
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- Action Row ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Camera Button
                Button(onClick = {
                    val uri = createTempImageUri(context)
                    capturedImageUri = uri
                    cameraLauncher.launch(uri)
                }) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Camera")
                }

                // 2. Gallery Button
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Gallery")
                }

                // 3. Refresh Button
                FilledIconButton(
                    onClick = {
                        // Never return current item or won items
                        val remaining = targetKeys.filter {
                            it != targetObject && it !in persistedWonTargets
                        }
                        val next = if (remaining.isNotEmpty()) {
                            remaining.random()
                        } else {
                            // All non-won items exhausted, cycle excluding only won items
                            persistedUsedTargets = mutableSetOf(targetObject)
                            targetKeys.filter {
                                it != targetObject && it !in persistedWonTargets
                            }.randomOrNull() ?: targetKeys.filter { it != targetObject }.random()
                        }
                        persistedUsedTargets.add(next)
                        targetObject = next
                        showSecondHint = false
                        statusMessage = "Target Refreshed!"
                    }
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Refresh Target")
                }
            }
        } // End of Column

        // --- Confetti View ---
        if (showConfetti) {
            val party = Party(
                speed = 0f, maxSpeed = 30f, damping = 0.9f, spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                position = Position.Relative(0.5, 0.3),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
            )
            KonfettiView(modifier = Modifier.fillMaxSize(), parties = listOf(party))
        }
    }
}