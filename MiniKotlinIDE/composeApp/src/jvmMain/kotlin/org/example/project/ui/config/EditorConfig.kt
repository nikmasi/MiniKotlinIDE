package org.example.project.ui.config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object EditorConfig {
    val fontSize = 14.sp
    val lineHeight = 20.sp
    val lineHeightDp = 20.dp

    val textStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = fontSize,
        lineHeight = lineHeight,
        color = Color.White
    )

    val lineNumberColor = Color.DarkGray
    val backgroundColor = Color(0xFF1E1E1E)

    // output pane
    val errorColor = Color(0xFFF44336)
    val errorLinkStyles = androidx.compose.ui.text.TextLinkStyles(
        style = androidx.compose.ui.text.SpanStyle(
            color = errorColor,
            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
        ),
        hoveredStyle = androidx.compose.ui.text.SpanStyle(
            background = errorColor.copy(alpha = 0.15f),
            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
        )
    )
}