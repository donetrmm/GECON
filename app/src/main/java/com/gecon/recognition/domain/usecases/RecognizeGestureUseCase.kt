package com.gecon.recognition.domain.usecases

import android.graphics.Bitmap
import android.net.Uri
import com.gecon.core.ml.DetectionResult
import com.gecon.core.ml.RecognitionResult
import com.gecon.recognition.domain.repository.GestureRecognitionRepository
import javax.inject.Inject

class RecognizeGestureUseCase @Inject constructor(
    private val repository: GestureRecognitionRepository
) {
    suspend operator fun invoke(bitmap: Bitmap): Result<List<RecognitionResult>> {
        return try {
            val results = repository.recognizeGesture(bitmap)
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fromUri(uri: Uri): Result<Pair<Bitmap, List<RecognitionResult>>> {
        return try {
            val detectionResult = repository.loadImageWithDetection(uri)
                ?: throw IllegalArgumentException("No se pudo cargar la imagen")

            val results = repository.recognizeGesture(detectionResult.croppedBitmap)

            val bitmapWithOverlay = drawDetectionOverlay(detectionResult)

            Result.success(Pair(bitmapWithOverlay, results))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun drawDetectionOverlay(detectionResult: DetectionResult): Bitmap {
        return detectionResult.originalBitmap
    }

    suspend fun fromExampleImage(fileName: String): Result<Pair<Bitmap, List<RecognitionResult>>> {
        return try {
            val bitmap = repository.loadExampleImage(fileName)
                ?: throw IllegalArgumentException("No se pudo cargar la imagen de ejemplo")

            val detectionResult = repository.recognizeGesture(bitmap)
            Result.success(Pair(bitmap, detectionResult))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}