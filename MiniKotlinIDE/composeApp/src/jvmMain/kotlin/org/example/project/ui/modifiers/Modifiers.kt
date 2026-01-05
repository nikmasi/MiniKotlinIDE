package org.example.project.ui.modifiers

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.paneScroll(
    verticalState: ScrollState,
    horizontalState: ScrollState,
    verticalPadding: Dp = 8.dp
): Modifier = this
    .fillMaxSize()
    .padding(vertical = verticalPadding)
    .horizontalScroll(horizontalState)
    .verticalScroll(verticalState)

fun Modifier.editorLineNumberStyle(
    verticalScrollState: ScrollState,
    verticalPadding: Dp = 8.dp,
    horizontalPadding: Dp = 12.dp
): Modifier = this
    .fillMaxHeight()
    .verticalScroll(verticalScrollState)
    .padding(vertical = verticalPadding, horizontal = horizontalPadding)

fun Modifier.defaultPaneScroll(
    verticalState: ScrollState,
    horizontalState: ScrollState
): Modifier = this
    .fillMaxSize()
    .verticalScroll(verticalState)
    .horizontalScroll(horizontalState)

fun Modifier.paddingStyle(
    paddingValues: PaddingValues
): Modifier = this.padding(paddingValues)
    .padding(bottom = 8.dp)