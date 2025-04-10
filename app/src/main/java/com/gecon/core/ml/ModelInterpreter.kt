package com.gecon.core.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject

class ModelInterpreter @Inject constructor(
    private val context: Context
) {
    private var interpreter: Interpreter? = null
    private val inputWidth = 224
    private val inputHeight = 224

    init {
        loadModel()
    }

    private fun loadModel() {
        try {
            Log.d("aaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaaaa:")

            val files = context.assets.list("models")
            files?.forEach { Log.d("Assets", "Archivo encontrado: $it") }
            val assetFileDescriptor = context.assets.openFd("models/gecon_final.tflite")
            val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel = fileInputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength

            val mappedByteBuffer: MappedByteBuffer = fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                startOffset,
                declaredLength
            )

            interpreter = Interpreter(mappedByteBuffer)
            Log.d("ModelInterpreter", "Modelo cargado correctamente")

        } catch (e: Exception) {
            Log.e("ModelInterpreter", "Error al cargar el modelo", e)
        }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }

    fun classify(bitmap: Bitmap): FloatArray {
        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap,
            inputWidth,
            inputHeight,
            true
        )

        val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)
        val outputArray = Array(1) { FloatArray(13) }

        interpreter?.run(byteBuffer, outputArray)

        return outputArray[0]
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(
            4 * inputWidth * inputHeight * 3
        ).apply {
            order(ByteOrder.nativeOrder())
        }

        val pixels = IntArray(inputWidth * inputHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixel in pixels) {
            val r = (pixel shr 16 and 0xFF) / 255.0f
            val g = (pixel shr 8 and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        return byteBuffer
    }
}