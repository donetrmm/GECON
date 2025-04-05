package com.gecon.recognition.data.model

import android.graphics.Bitmap
import android.net.Uri
import com.gecon.core.ml.RecognitionResult

data class GestureRecognitionData(
    val imageUri: Uri? = null,
    val imageBitmap: Bitmap? = null,
    val recognitionResults: List<RecognitionResult> = emptyList(),
    val isProcessing: Boolean = false,
    val error: String? = null
)