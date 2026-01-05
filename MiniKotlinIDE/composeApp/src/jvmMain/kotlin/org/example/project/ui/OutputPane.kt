package org.example.project.ui

import androidx.compose.foundation.ScrollState
import org.example.project.ui.components.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.ui.unit.dp
import org.example.project.model.ScriptError
import org.example.project.parser.ScriptParser
import org.example.project.ui.config.EditorConfig
import androidx.compose.material3.Text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.withLink
import org.example.project.ui.modifiers.defaultPaneScroll

@Composable
fun OutputPane(
    output: String,
    onErrorClick: (ScriptError) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val scrollState2 = rememberScrollState()

    val errorMap = remember(output) {
        ScriptParser.parseErrors(output).associateBy { it.rawText.trim() }
    }

    LaunchedEffect(output) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    val annotatedText = rememberAnnotatedOutput(output, errorMap, onErrorClick)

    Box(modifier.background(EditorConfig.backgroundColor).padding(start = 8.dp, top=8.dp, bottom = 8.dp)) {
        // selection container
        OutputSelectionContainer(
            annotatedText = annotatedText,
            verticalState = scrollState,
            horizontalState = scrollState2
        )

        // vertical scrollbar
        PaneVerticalScrollbar(scrollState)

        // horizontal scrollbar
        PaneHorizontalScrollbar(scrollState2)
    }
}

@Composable
private fun rememberAnnotatedOutput(
    output: String,
    errorMap: Map<String, ScriptError>,
    onErrorClick: (ScriptError) -> Unit
) = remember(output, errorMap) {
    val linkStyle = EditorConfig.errorLinkStyles

    buildAnnotatedString {
        val lines = output.lines()
        lines.forEachIndexed { index, line ->
            val trimmedLine = line.trim()
            val error = errorMap[trimmedLine]

            if (error != null) {
                val link = LinkAnnotation.Clickable(
                    tag = "ERROR",
                    styles = linkStyle,
                    linkInteractionListener = { onErrorClick(error) }
                )
                withLink(link) { append(line) }
            } else {
                append(line)
            }
            if (index < lines.lastIndex) append("\n")
        }
    }
}

@Composable
private fun OutputSelectionContainer (
    annotatedText: AnnotatedString,
    verticalState: ScrollState,
    horizontalState: ScrollState,
) {
    SelectionContainer {
        Text(
            text = annotatedText,
            style = EditorConfig.textStyle,
            modifier = Modifier.defaultPaneScroll(verticalState,horizontalState)
        )
    }
}