package org.example.project.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.key
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.ui.components.PaneVerticalScrollbar
import org.example.project.ui.config.EditorConfig
import org.example.project.ui.modifiers.editorLineNumberStyle
import org.example.project.ui.modifiers.paneScroll
import androidx.compose.ui.input.key.Key
import org.example.project.ui.config.KotlinHighlighter

@Composable
fun EditorPane(
    modifier: Modifier,
    cursorPos: Pair<Int, Int>?,
    onTextChange: (String) -> Unit,
    scriptText: String
){
    Box(modifier.background(MaterialTheme.colorScheme.surface)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // code editor
            CodeEditor(
                text = scriptText,
                onTextChange = onTextChange,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                cursorPos = cursorPos
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CodeEditor(
    text: String,
    onTextChange: (String) -> Unit,
    cursorPos: Pair<Int, Int>?,
    modifier: Modifier = Modifier
) {
    var fieldValue by remember { mutableStateOf(TextFieldValue(text)) }
    var highlightedText by remember { mutableStateOf(AnnotatedString(text)) }

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val density = LocalDensity.current
    val lineHeightPx = with(density) { 20.dp.toPx() }

    LaunchedEffect(fieldValue.text) {
        highlightedText = KotlinHighlighter.highlight(fieldValue.text)
    }

    LaunchedEffect(cursorPos) {
        cursorPos?.let { (line, column) ->
            val lines = fieldValue.text.lines()
            var index = 0
            for (i in 0 until (line - 1).coerceAtMost(lines.size)) {
                index += lines[i].length + 1
            }
            index += (column - 1).coerceIn(0, lines.getOrNull(line - 1)?.length ?: 0)

            fieldValue = fieldValue.copy(
                selection = TextRange(index.coerceIn(0, fieldValue.text.length))
            )

            verticalScrollState.animateScrollTo(((line - 1) * lineHeightPx).toInt())
        }
    }

    LaunchedEffect(fieldValue.selection) {
        val cursorPosition = fieldValue.selection.start
        val textBeforeCursor = fieldValue.text.substring(0, cursorPosition)
        val lineAtCursor = textBeforeCursor.count { it == '\n' }

        val cursorYStart = lineAtCursor * lineHeightPx
        val cursorYEnd = (lineAtCursor + 1) * lineHeightPx

        if (cursorYEnd > verticalScrollState.value + verticalScrollState.viewportSize && verticalScrollState.viewportSize > 0) {
            verticalScrollState.scrollTo((cursorYEnd - verticalScrollState.viewportSize).toInt())
        } else if (cursorYStart < verticalScrollState.value) {
            verticalScrollState.scrollTo(cursorYStart.toInt())
        }

        val currentLineText = textBeforeCursor.substringAfterLast('\n')
        val charWidthEstimate = with(density) { 8.5.sp.toPx() }
        val cursorX = currentLineText.length * charWidthEstimate

        if (cursorX > horizontalScrollState.value + horizontalScrollState.viewportSize && horizontalScrollState.viewportSize > 0) {
            horizontalScrollState.scrollTo((cursorX - horizontalScrollState.viewportSize + 100).toInt())
        } else if (cursorX < horizontalScrollState.value) {
            horizontalScrollState.scrollTo(cursorX.toInt())
        }
    }


    Box(modifier = modifier.background(EditorConfig.backgroundColor)) {
        Row(modifier = Modifier.fillMaxSize()) {

            // line numbers
            EditorLineNumbers(
                lineCount = fieldValue.text.count { it == '\n' } + 1,
                verticalScrollState = verticalScrollState,
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color.White.copy(alpha = 0.1f))
            )
            Spacer(modifier = Modifier.width(2.dp))

            // basic editor
            BasicTextField(
                value = fieldValue,
                onValueChange = { newValue ->
                    val oldText = fieldValue.text
                    val newText = newValue.text

                    val selection = newValue.selection.start
                    val lastChar = if (selection > 0 && newText.length > oldText.length) {
                        newText[selection - 1]
                    } else null

                    val processedValue = when (lastChar) {
                        '(' -> insertPair(newValue, ")")
                        '{' -> insertPair(newValue, "}")
                        '[' -> insertPair(newValue, "]")
                        '"' -> insertPair(newValue, "\"")
                        '\'' -> insertPair(newValue, "'")
                        else -> newValue
                    }

                    fieldValue = processedValue
                    if (processedValue.text != text) {
                        onTextChange(processedValue.text)
                    }
                },
                modifier = Modifier
                    .paneScroll(verticalScrollState, horizontalScrollState)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown) {
                            when (keyEvent.key) {
                                Key.Tab -> {
                                    val tabSpaces = "    "
                                    val selection = fieldValue.selection.start
                                    val newText = StringBuilder(fieldValue.text)
                                        .insert(selection, tabSpaces).toString()

                                    fieldValue = fieldValue.copy(
                                        text = newText,
                                        selection = TextRange(selection + tabSpaces.length)
                                    )
                                    onTextChange(newText)
                                    true
                                }

                                Key.Enter -> {
                                    val selection = fieldValue.selection.start
                                    val text = fieldValue.text

                                    if (selection > 0 && text[selection - 1] == '{') {
                                        val indentText = "\n    \n}"
                                        val newText = StringBuilder(text)
                                            .insert(selection, indentText).toString()

                                        fieldValue = fieldValue.copy(
                                            text = newText,
                                            selection = TextRange(selection + 5)
                                        )
                                        onTextChange(newText)
                                        true
                                    } else {
                                        false
                                    }
                                }
                                else -> false
                            }
                        } else {
                            false
                        }
                    }
                ,
                textStyle = EditorConfig.textStyle.copy(color = Color.Transparent),
                cursorBrush = SolidColor(Color.Cyan),
                decorationBox = { innerTextField ->
                    Box {
                        Text(
                            text = highlightedText,
                            style = EditorConfig.textStyle,
                            softWrap = false
                        )
                        innerTextField()
                    }
                }
            )
        }

        // vertical scrollbar
        PaneVerticalScrollbar(verticalScrollState)
    }
}

@Composable
private fun EditorLineNumbers(lineCount: Int, verticalScrollState: ScrollState){
    Column(
        modifier = Modifier.editorLineNumberStyle(verticalScrollState),
        horizontalAlignment = Alignment.End
    ) {
        repeat(lineCount) { i ->
            Text(
                text = (i + 1).toString(),
                color = EditorConfig.lineNumberColor,
                style = EditorConfig.textStyle,
                modifier = Modifier.height(EditorConfig.lineHeightDp).graphicsLayer()
            )
        }
    }
}

private fun insertPair(value: TextFieldValue, closingChar: String): TextFieldValue {
    val selection = value.selection.start
    val newText = StringBuilder(value.text).insert(selection, closingChar).toString()

    return value.copy(
        text = newText,
        selection = TextRange(selection)
    )
}