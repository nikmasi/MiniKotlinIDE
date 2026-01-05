package org.example.project.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun VerticalPanelDivider(onWidthChange: (Float) -> Unit) {
    Box(
        modifier = Modifier
            .width(6.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.outlineVariant)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    onWidthChange(dragAmount)
                }
            }
    )
}