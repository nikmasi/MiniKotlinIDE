package org.example.project.lexer


enum class TokenType {
    HARD_KEYWORD,
    SOFT_KEYWORD,
    MODIFIER,
    IDENTIFIER,
    STRING,
    NUMBER,
    COMMENT,
    OPERATOR,
    WHITESPACE,
    UNKNOWN
}
data class Token(
    val type: TokenType,
    val text: String,
    val start: Int,
    val end: Int
)