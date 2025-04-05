package com.gecon.core.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class ImageLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imagePreprocess: ImagePreprocess
) {

    suspend fun loadImage(uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        return@withContext imagePreprocess.preprocessImage(uri)
    }

    suspend fun loadAssetImage(fileName: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            context.assets.open(fileName).use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                return@withContext bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return@withContext null
    }

    suspend fun loadExampleImage(fileName: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            context.assets.open("example_images/$fileName").use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                return@withContext bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return@withContext null
    }
}