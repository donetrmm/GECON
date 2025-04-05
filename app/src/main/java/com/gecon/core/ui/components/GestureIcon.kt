package com.gecon.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GestureIcon(
    iconResName: String,
    modifier: Modifier = Modifier
) {

    val icon = when (iconResName) {
        "ic_smile" -> Icons.Default.Face
        "ic_frown" -> Icons.Default.Face
        "ic_wink_right" -> Icons.Default.Face
        "ic_wink_left" -> Icons.Default.Face
        "ic_eyebrows" -> Icons.Default.Face
        "ic_hand_open" -> Icons.Default.ThumbUp
        "ic_hand_closed" -> Icons.Default.ThumbUp
        "ic_thumb_up" -> Icons.Default.ThumbUp
        "ic_thumb_down" -> Icons.Default.ThumbDown
        "ic_two_fingers" -> Icons.Default.ThumbUp
        "ic_palm" -> Icons.Default.ThumbUp
        "ic_ok" -> Icons.Default.ThumbUp
        "ic_circle" -> Icons.Default.ThumbUp
        else -> Icons.Default.Face
    }

    val backgroundColor = when (iconResName) {
        "ic_smile", "ic_frown", "ic_wink_right", "ic_wink_left", "ic_eyebrows" ->
            MaterialTheme.colorScheme.primaryContainer
        else ->
            MaterialTheme.colorScheme.secondaryContainer
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icono de gesto",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}