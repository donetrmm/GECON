package com.gecon.core.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CompletableDeferred

data class DetectionResult(
    val croppedBitmap: Bitmap,
    val originalBitmap: Bitmap,
    val boundingBox: RectF,
    val detectionType: DetectionType
)

enum class DetectionType {
    FACE, HAND, NONE
}

@Singleton
class GestureDetector @Inject constructor(
    private val context: Context
) {
    private val faceDetectorOptions = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    private val faceDetector = FaceDetection.getClient(faceDetectorOptions)

    private var handLandmarker: HandLandmarker? = null

    init {
        try {
            val handBaseOptions = BaseOptions.builder()
                .setDelegate(Delegate.CPU)
                .build()

            val handOptions = HandLandmarker.HandLandmarkerOptions.builder()
                .setBaseOptions(handBaseOptions)
                .setNumHands(2)
                .setMinHandDetectionConfidence(0.5f)
                .setMinHandPresenceConfidence(0.5f)
                .setMinTrackingConfidence(0.5f)
                .setRunningMode(RunningMode.IMAGE)
                .build()

            handLandmarker = HandLandmarker.createFromOptions(context, handOptions)

            Log.d("GestureDetector", "MediaPipe inicializado correctamente")
        } catch (e: Exception) {
            Log.e("GestureDetector", "Error al inicializar MediaPipe", e)
        }
    }

    suspend fun detectAndCropGesture(bitmap: Bitmap): DetectionResult = withContext(Dispatchers.Default) {
        
        detectHandWithMediaPipe(bitmap)?.let { return@withContext it }

        
        detectFace(bitmap)?.let { return@withContext it }

        
        return@withContext DetectionResult(
            croppedBitmap = bitmap,
            originalBitmap = bitmap,
            boundingBox = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat()),
            detectionType = DetectionType.NONE
        )
    }

    private suspend fun detectFace(bitmap: Bitmap): DetectionResult? {
        val deferred = CompletableDeferred<DetectionResult?>()

        val image = InputImage.fromBitmap(bitmap, 0)

        faceDetector.process(image)
            .addOnSuccessListener { faces ->
                if (faces.isEmpty()) {
                    deferred.complete(null)
                    return@addOnSuccessListener
                }

                
                val face = faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() }

                if (face == null) {
                    deferred.complete(null)
                    return@addOnSuccessListener
                }

                val faceBox = face.boundingBox

                
                val expandFactor = 0.4f
                val width = faceBox.width()
                val height = faceBox.height()
                val paddingX = (width * expandFactor).toInt()
                val paddingY = (height * expandFactor).toInt()

                
                val left = maxOf(0, faceBox.left - paddingX)
                val top = maxOf(0, faceBox.top - paddingY)
                val right = minOf(bitmap.width, faceBox.right + paddingX)
                val bottom = minOf(bitmap.height, faceBox.bottom + paddingY)

                try {
                    val croppedBitmap = Bitmap.createBitmap(
                        bitmap,
                        left,
                        top,
                        right - left,
                        bottom - top
                    )

                    val boundingBox = RectF(
                        left.toFloat(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat()
                    )

                    deferred.complete(
                        DetectionResult(
                            croppedBitmap = croppedBitmap,
                            originalBitmap = bitmap,
                            boundingBox = boundingBox,
                            detectionType = DetectionType.FACE
                        )
                    )
                } catch (e: Exception) {
                    Log.e("GestureDetector", "Error al recortar cara: ${e.message}")
                    deferred.complete(null)
                }
            }
            .addOnFailureListener {
                Log.e("GestureDetector", "Error detectando caras: ${it.message}")
                deferred.complete(null)
            }

        return deferred.await()
    }

    private suspend fun detectHandWithMediaPipe(bitmap: Bitmap): DetectionResult? = withContext(Dispatchers.Default) {
        try {
            val mpImage = BitmapImageBuilder(bitmap).build()
            val result = handLandmarker?.detect(mpImage)

            if (result == null || result.landmarks().isEmpty()) {
                return@withContext null
            }

            val boundingBox = getBoundingBoxFromHandLandmarks(result, bitmap.width, bitmap.height)

            if (boundingBox == null) {
                return@withContext null
            }

            
            val paddingPixels = 40 

            val left = maxOf(0f, boundingBox.left - paddingPixels)
            val top = maxOf(0f, boundingBox.top - paddingPixels)
            val right = minOf(bitmap.width.toFloat(), boundingBox.right + paddingPixels)
            val bottom = minOf(bitmap.height.toFloat(), boundingBox.bottom + paddingPixels)

            
            
            val cropWidth = right - left
            val cropHeight = bottom - top
            val cropArea = cropWidth * cropHeight
            val imageArea = bitmap.width * bitmap.height

            val minAreaRatio = 0.15f

            val finalLeft: Float
            val finalTop: Float
            val finalRight: Float
            val finalBottom: Float

            if (cropArea / imageArea < minAreaRatio) {
                
                val centerX = (left + right) / 2
                val centerY = (top + bottom) / 2

                val targetArea = imageArea * minAreaRatio
                val scaleFactor = Math.sqrt((targetArea / cropArea).toDouble()).toFloat()

                val newWidth = cropWidth * scaleFactor
                val newHeight = cropHeight * scaleFactor

                finalLeft = maxOf(0f, centerX - newWidth / 2)
                finalTop = maxOf(0f, centerY - newHeight / 2)
                finalRight = minOf(bitmap.width.toFloat(), centerX + newWidth / 2)
                finalBottom = minOf(bitmap.height.toFloat(), centerY + newHeight / 2)
            } else {
                finalLeft = left
                finalTop = top
                finalRight = right
                finalBottom = bottom
            }

            try {
                val croppedBitmap = Bitmap.createBitmap(
                    bitmap,
                    finalLeft.toInt(),
                    finalTop.toInt(),
                    (finalRight - finalLeft).toInt(),
                    (finalBottom - finalTop).toInt()
                )

                val finalBoundingBox = RectF(finalLeft, finalTop, finalRight, finalBottom)

                return@withContext DetectionResult(
                    croppedBitmap = croppedBitmap,
                    originalBitmap = bitmap,
                    boundingBox = finalBoundingBox,
                    detectionType = DetectionType.HAND
                )
            } catch (e: Exception) {
                Log.e("GestureDetector", "Error al recortar mano: ${e.message}")
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e("GestureDetector", "Error en detección con MediaPipe: ${e.message}")
            return@withContext null
        }
    }

    private fun getBoundingBoxFromHandLandmarks(result: HandLandmarkerResult, imageWidth: Int, imageHeight: Int): RectF? {
        if (result.landmarks().isEmpty()) return null

        val landmarks = result.landmarks()[0]

        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE

        for (landmark in landmarks) {
            val x = landmark.x() * imageWidth
            val y = landmark.y() * imageHeight

            minX = minOf(minX, x)
            minY = minOf(minY, y)
            maxX = maxOf(maxX, x)
            maxY = maxOf(maxY, y)
        }

        return RectF(minX, minY, maxX, maxY)
    }

    fun drawDetectionOverlay(bitmap: Bitmap, boundingBox: RectF, detectionType: DetectionType): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint().apply {
            color = when (detectionType) {
                DetectionType.FACE -> Color.GREEN
                DetectionType.HAND -> Color.BLUE
                DetectionType.NONE -> Color.RED
            }
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }

        canvas.drawRect(boundingBox, paint)

        return mutableBitmap
    }

    fun close() {
        handLandmarker?.close()
    }
}