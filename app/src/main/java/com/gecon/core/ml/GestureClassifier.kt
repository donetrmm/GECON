package com.gecon.core.ml

import android.graphics.Bitmap
import javax.inject.Inject

data class RecognitionResult(
    val gestureInfo: GestureInfo,
    val confidence: Float
)

class GestureClassifier @Inject constructor(
    private val modelInterpreter: ModelInterpreter,
    private val gestureMapper: GestureMapper
) {

    fun classifyImage(bitmap: Bitmap): RecognitionResult {
        val outputArray = modelInterpreter.classify(bitmap)

        var maxIndex = 0
        var maxConfidence = outputArray[0]

        for (i in 1 until outputArray.size) {
            if (outputArray[i] > maxConfidence) {
                maxConfidence = outputArray[i]
                maxIndex = i
            }
        }

        val gestureInfo = gestureMapper.mapIndexToGesture(maxIndex)

        return RecognitionResult(gestureInfo, maxConfidence)
    }

    fun getTopNResults(bitmap: Bitmap, topN: Int = 3): List<RecognitionResult> {
        val outputArray = modelInterpreter.classify(bitmap)
        val indexedOutput = outputArray.withIndex().sortedByDescending { it.value }

        return indexedOutput.take(topN).map { (index, confidence) ->
            RecognitionResult(gestureMapper.mapIndexToGesture(index), confidence)
        }
    }

    fun release() {
        modelInterpreter.close()
    }
}