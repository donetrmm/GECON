package com.gecon.recognition.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gecon.recognition.data.model.GestureRecognitionData
import com.gecon.recognition.domain.repository.GestureRecognitionRepository
import com.gecon.recognition.domain.usecases.RecognizeGestureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecognitionViewModel @Inject constructor(
    private val recognizeGestureUseCase: RecognizeGestureUseCase,
    private val gestureRecognitionRepository: GestureRecognitionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GestureRecognitionData())
    val state: StateFlow<GestureRecognitionData> = _state.asStateFlow()

    fun recognizeFromUri(uri: Uri) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, error = null) }

            try {
                val detectionResult = gestureRecognitionRepository.loadImageWithDetection(uri)

                if (detectionResult != null) {
                    val recognitionResults = gestureRecognitionRepository.recognizeGesture(detectionResult.croppedBitmap)

                    _state.update {
                        it.copy(
                            imageUri = uri,
                            imageBitmap = detectionResult.originalBitmap,
                            recognitionResults = recognitionResults,
                            boundingBox = detectionResult.boundingBox,
                            detectionType = detectionResult.detectionType,
                            isProcessing = false,
                            error = null
                        )
                    }
                } else {
                    val bitmap = gestureRecognitionRepository.loadImage(uri)
                    if (bitmap != null) {
                        val recognitionResults = gestureRecognitionRepository.recognizeGesture(bitmap)

                        _state.update {
                            it.copy(
                                imageUri = uri,
                                imageBitmap = bitmap,
                                recognitionResults = recognitionResults,
                                isProcessing = false,
                                error = null
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isProcessing = false,
                                error = "No se pudo cargar la imagen"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isProcessing = false,
                        error = e.message ?: "Error desconocido al procesar la imagen"
                    )
                }
            }
        }
    }

    fun clearData() {
        _state.update {
            GestureRecognitionData()
        }
    }
}