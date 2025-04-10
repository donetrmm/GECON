package com.gecon.recognition.presentation

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gecon.core.image.ImagePicker
import com.gecon.core.ui.components.GestureCard
import com.gecon.recognition.presentation.components.ImageWithOverlay
import com.gecon.recognition.presentation.components.CommandFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognitionScreen(
    viewModel: RecognitionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Reconocimiento de gestos") },
                actions = {
                    if (state.imageBitmap != null) {
                        IconButton(onClick = { viewModel.clearData() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.isProcessing) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = "Procesando imagen...",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 60.dp)
                            )
                        }
                    }
                } else if (state.imageBitmap != null) {
                    item {
                        ImageWithOverlay(
                            bitmap = state.imageBitmap!!,
                            topResult = state.recognitionResults.firstOrNull(),
                            boundingBox = state.boundingBox,
                            detectionType = state.detectionType
                        )
                    }

                    if (state.recognitionResults.isNotEmpty()) {
                        item {
                            Text(
                                text = "Resultado del reconocimiento",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        items(state.recognitionResults) { result ->
                            GestureCard(
                                gestureInfo = result.gestureInfo,
                                confidence = result.confidence
                            )
                        }

                        state.recognitionResults.firstOrNull()?.let { topResult ->
                            item {
                                CommandFeedback(
                                    gestureInfo = topResult.gestureInfo,
                                    confidence = topResult.confidence
                                )
                            }
                        }

                        item {
                            state.detectionType?.let { type ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Tipo de detección: ${
                                            when (type) {
                                                com.gecon.core.ml.DetectionType.FACE -> "Facial"
                                                com.gecon.core.ml.DetectionType.HAND -> "Manual"
                                                else -> "Ninguna"
                                            }
                                        }",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            Text(
                                text = "No se detectaron gestos",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    state.error?.let { error ->
                        item {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    item {
                        ImagePicker(
                            onImageSelected = { uri ->
                                viewModel.recognizeFromUri(uri)
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
    }
}