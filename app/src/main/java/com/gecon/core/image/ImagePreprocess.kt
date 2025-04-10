package com.gecon.core.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.gecon.core.ml.DetectionResult
import com.gecon.core.ml.GestureDetector
import java.io.IOException
import javax.inject.Inject

class ImagePreprocess @Inject constructor(
    private val context: Context,
    private val gestureDetector: GestureDetector
) {
    suspend fun preprocessImage(uri: Uri): Bitmap? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use {
                val bitmap = BitmapFactory.decodeStream(it)
                val rotatedBitmap = correctImageOrientation(uri, bitmap)
                val resizedBitmap = resizeBitmapIfNeeded(rotatedBitmap)

                
                val detectionResult = gestureDetector.detectAndCropGesture(resizedBitmap)

                return detectionResult.croppedBitmap
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun preprocessImageWithDetection(uri: Uri): DetectionResult? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use {
                val bitmap = BitmapFactory.decodeStream(it)
                val rotatedBitmap = correctImageOrientation(uri, bitmap)
                val resizedBitmap = resizeBitmapIfNeeded(rotatedBitmap)

                
                return gestureDetector.detectAndCropGesture(resizedBitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun correctImageOrientation(uri: Uri, bitmap: Bitmap): Bitmap {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use {
                val exif = ExifInterface(it)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )

                val matrix = Matrix()
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
                    ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
                    else -> return bitmap
                }

                return Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun resizeBitmapIfNeeded(bitmap: Bitmap, maxSize: Int = 1024): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }

        val ratio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int

        if (width > height) {
            newWidth = maxSize
            newHeight = (maxSize / ratio).toInt()
        } else {
            newHeight = maxSize
            newWidth = (maxSize * ratio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}