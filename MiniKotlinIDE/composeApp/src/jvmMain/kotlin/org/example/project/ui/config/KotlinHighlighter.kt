package org.example.project.ui.config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import org.example.project.lexer.KotlinLexer
import org.example.project.lexer.TokenType

object KotlinHighlighter {
    fun highlight(text: String): AnnotatedString {
        val tokens = KotlinLexer(text).tokenize()

        val builder = AnnotatedString.Builder(text)

        for (token in tokens) {
            val color = when (token.type) {
                TokenType.HARD_KEYWORD -> Color(0xFF569CD6) // blue
                TokenType.SOFT_KEYWORD -> Color(0xFF569CD6) //
                TokenType.MODIFIER -> Color(0xFFC586C0)     // purple
                TokenType.STRING -> Color(0xFFCE9178)       // orange
                TokenType.COMMENT -> Color(0xFF6A9955)      // green
                TokenType.NUMBER -> Color(0xFFB5CEA8)       // light green
                else -> Color.White
            }

            builder.addStyle(
                SpanStyle(color = color),
                token.start,
                token.end
            )
        }

        return builder.toAnnotatedString()
    }
}