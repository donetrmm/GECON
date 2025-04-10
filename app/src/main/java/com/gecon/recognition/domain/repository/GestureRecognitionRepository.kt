package com.gecon.recognition.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.gecon.core.ml.DetectionResult
import com.gecon.core.ml.RecognitionResult

interface GestureRecognitionRepository {
    suspend fun recognizeGesture(bitmap: Bitmap): List<RecognitionResult>
    suspend fun loadImage(uri: Uri): Bitmap?
    suspend fun loadImageWithDetection(uri: Uri): DetectionResult?
    suspend fun loadExampleImage(fileName: String): Bitmap?
}