package com.gecon.recognition.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gecon.recognition.data.model.GestureRecognitionData
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
    private val recognizeGestureUseCase: RecognizeGestureUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GestureRecognitionData())
    val state: StateFlow<GestureRecognitionData> = _state.asStateFlow()

    fun recognizeFromUri(uri: Uri) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, error = null) }

            recognizeGestureUseCase.fromUri(uri).fold(
                onSuccess = { (bitmap, results) ->
                    _state.update {
                        it.copy(
                            imageUri = uri,
                            imageBitmap = bitmap,
                            recognitionResults = results,
                            isProcessing = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            error = error.message ?: "Error desconocido al procesar la imagen"
                        )
                    }
                }
            )
        }
    }

    fun clearData() {
        _state.update {
            GestureRecognitionData()
        }
    }
}