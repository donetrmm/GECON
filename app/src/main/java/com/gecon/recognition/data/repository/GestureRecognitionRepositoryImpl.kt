package com.gecon.recognition.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.gecon.core.image.ImageLoader
import com.gecon.core.ml.DetectionResult
import com.gecon.core.ml.GestureClassifier
import com.gecon.core.ml.GestureDetector
import com.gecon.core.ml.RecognitionResult
import com.gecon.recognition.domain.repository.GestureRecognitionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GestureRecognitionRepositoryImpl @Inject constructor(
    private val gestureClassifier: GestureClassifier,
    private val imageLoader: ImageLoader,
    private val gestureDetector: GestureDetector
) : GestureRecognitionRepository {

    override suspend fun recognizeGesture(bitmap: Bitmap): List<RecognitionResult> {
        return withContext(Dispatchers.Default) {
            val detectionResult = gestureDetector.detectAndCropGesture(bitmap)
            gestureClassifier.getTopNResults(detectionResult.croppedBitmap, 3)
        }
    }

    override suspend fun loadImage(uri: Uri): Bitmap? {
        return imageLoader.loadImage(uri)
    }

    override suspend fun loadImageWithDetection(uri: Uri): DetectionResult? {
        return imageLoader.loadImageWithDetection(uri)
    }

    override suspend fun loadExampleImage(fileName: String): Bitmap? {
        return imageLoader.loadExampleImage(fileName)
    }
}