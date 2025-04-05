package com.gecon.home.presentation

import androidx.lifecycle.ViewModel
import com.gecon.core.ml.GestureInfo
import com.gecon.core.ml.GestureMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gestureMapper: GestureMapper
) : ViewModel() {

    val faceGestures: List<GestureInfo> = gestureMapper.getAllGestures()
        .filter { it.id in 1..5 }

    val handGestures: List<GestureInfo> = gestureMapper.getAllGestures()
        .filter { it.id in 6..13 }
}