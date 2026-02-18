package com.example.mobilegame

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameScreen(username: String, category: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val categoryTargets = remember {
        mapOf(
            "Nature" to listOf(
                "Plant", "Flower", "Sky", "Twig", "Bird", "Branch", "Soil"
            ),
            "Kitchen" to listOf(
                "Coffee", "Food", "Tableware", "Cookware and bakeware",
                "Cuisine", "Couscous", "Cake", "Bread", "Vegetable", "Fruit",
                "Juice", "Pizza", "Sushi"
            ),
            "Office" to listOf(
                "Television", "Chair", "Mobile phone", "Desk", "Computer",
                "Whiteboard", "Blackboard", "Poster", "Cabinetry"
            ),
            "Pets" to listOf(
                "Dog", "Cat", "Bird", "Cattle", "Gerbil",
                "Horse", "Pet"
            )
        )
    }
    
    val targets = categoryTargets[category] ?: categoryTargets["Office"]!!
    var targetObject by remember { mutableStateOf(targets.random()) }

    var statusMessage by remember { mutableStateOf("Find a $targetObject!") }
    var detectedLabels by remember { mutableStateOf<List<String>>(emptyList()) }
    var isProcessing by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            statusMessage = "Analyzing camera capture..."
            detectedLabels = emptyList()
            isProcessing = true
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            statusMessage = "Analyzing gallery image..."
            detectedLabels = emptyList()
            isProcessing = true
            analyzePhoto(context, it, targetObject) { found, confidence, labels ->
                detectedLabels = labels
                statusMessage = if (found) "Success! Found $targetObject (${(confidence * 100).toInt()}%)" else "Not found. Try again!"
                isProcessing = false
            }
        }
    }

    // Capture state for camera image URI
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Effect to analyze camera photo once captured
    LaunchedEffect(isProcessing) {
        if (isProcessing && capturedImageUri != null) {
            analyzePhoto(context, capturedImageUri!!, targetObject) { found, confidence, labels ->
                detectedLabels = labels
                statusMessage = if (found) "Success! Found $targetObject (${(confidence * 100).toInt()}%)" else "Not found. Try again!"
                isProcessing = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Scavenger Hunt",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Target:",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = targetObject,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (isProcessing) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Detected Objects:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(detectedLabels) { label ->
                Text(text = "• $label", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val uri = createTempImageUri(context)
                    capturedImageUri = uri
                    cameraLauncher.launch(uri)
                }
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Camera")
            }

            Button(
                onClick = {
                    galleryLauncher.launch("image/*")
                }
            ) {
                Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Gallery")
            }
            
            IconButton(
                onClick = {
                    targetObject = targets.random()
                    statusMessage = "Find a $targetObject!"
                    detectedLabels = emptyList()
                }
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "New Target")
            }
        }
    }
}


