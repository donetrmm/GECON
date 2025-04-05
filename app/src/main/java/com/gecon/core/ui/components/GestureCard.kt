package com.gecon.core.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gecon.core.ml.GestureInfo

@SuppressLint("DefaultLocale")
@Composable
fun GestureCard(
    modifier: Modifier = Modifier,
    gestureInfo: GestureInfo,
    confidence: Float? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GestureIcon(
                iconResName = gestureInfo.iconResName,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = gestureInfo.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Comando: ${gestureInfo.command}",
                    style = MaterialTheme.typography.bodyMedium
                )

                confidence?.let {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Confianza: ${String.format("%.1f", it * 100)}%",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}