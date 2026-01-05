package org.example.project.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Scaffold
import org.example.project.model.ScriptUiState
import org.example.project.ui.components.VerticalPanelDivider
import org.example.project.ui.modifiers.paddingStyle
import org.example.project.viewModel.ScriptViewModel

@Composable
@Preview
fun App() {
    //val viewModel: ScriptViewModel = viewModel()
    val viewModel = remember { ScriptViewModel() }

    MaterialTheme {
        val uiState by viewModel.uiState.collectAsState()
        val lastRun = viewModel.runHistory.firstOrNull()

        Scaffold(
            topBar = {
                TopBar(
                    status = uiState.status,
                    onRun = { viewModel.runScript() },
                    onStop = { viewModel.stopScript() },
                    lastRunDuration = lastRun?.duration
                )
            }
        ) { paddingValues ->

            MainArea(paddingValues, uiState.output,viewModel,uiState)
        }
    }
}

@Composable
private fun MainArea(paddingValues: PaddingValues, output: String, viewModel: ScriptViewModel, uiState: ScriptUiState) {
    val cursorPos by viewModel.cursorPosition.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val totalWidthPx = constraints.maxWidth.toFloat()
        var splitRatio by remember { mutableStateOf(0.5f) }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // editor
            EditorPane(
                modifier = Modifier
                    .fillMaxWidth(splitRatio)
                    .padding(paddingValues),
                cursorPos = cursorPos,
                onTextChange = { viewModel.updateScriptText(it) },
                scriptText = uiState.scriptText
            )

            // Divider
            VerticalPanelDivider(
                onWidthChange = { dragAmountPx ->
                    // px -> %
                    val deltaRatio = dragAmountPx / totalWidthPx
                    splitRatio = (splitRatio + deltaRatio).coerceIn(0.2f, 0.8f)
                }
            )

            // output
            OutputPane(
                output = output,
                modifier = Modifier.paddingStyle(paddingValues).weight(1f),
                onErrorClick = { error ->
                    viewModel.moveCursorTo(error.line, error.column)
                }
            )
        }
    }
}