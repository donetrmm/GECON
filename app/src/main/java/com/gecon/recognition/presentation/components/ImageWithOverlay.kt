package com.gecon.recognition.presentation.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.gecon.core.ml.DetectionType
import com.gecon.core.ml.RecognitionResult

@SuppressLint("DefaultLocale")
@Composable
fun ImageWithOverlay(
    bitmap: Bitmap,
    topResult: RecognitionResult?,
    boundingBox: RectF? = null,
    detectionType: DetectionType? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Imagen para reconocimiento",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.FillWidth
        )

        boundingBox?.let { box ->
            val boxColor = when (detectionType) {
                DetectionType.FACE -> Color.Green
                DetectionType.HAND -> Color.Blue
                else -> Color.Red
            }

            Canvas(
                modifier = Modifier.fillMaxWidth()
            ) {
                val scaleX = size.width / bitmap.width
                val scaleY = size.width / bitmap.width * bitmap.height / bitmap.width

                drawRect(
                    color = boxColor,
                    topLeft = Offset(
                        box.left * scaleX,
                        box.top * scaleY
                    ),
                    size = Size(
                        (box.right - box.left) * scaleX,
                        (box.bottom - box.top) * scaleY
                    ),
                    style = Stroke(width = 4f)
                )
            }
        }

        topResult?.let { result ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = "${result.gestureInfo.name} (${String.format("%.1f", result.confidence * 100)}%)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}