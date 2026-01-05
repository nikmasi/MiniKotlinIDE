package org.example.project.ui.components

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.PaneVerticalScrollbar(scrollState: ScrollState) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
    )
}

@Composable
fun BoxScope.PaneHorizontalScrollbar(scrollState: ScrollState) {
    HorizontalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(end = 12.dp)
    )
}