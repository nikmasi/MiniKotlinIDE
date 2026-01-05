package org.example.project.lexer

//https://kotlinlang.org/docs/keyword-reference.html#operators-and-special-symbols

class KotlinLexer(
    private val source: String
) {
    private var index = 0

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()

        while (index < source.length) {
            val start = index
            val c = source[index]

            when {
                c.isWhitespace() -> {
                    index++
                    tokens += Token(TokenType.WHITESPACE, c.toString(), start, index)
                }

                c == '/' && peek('/') -> {
                    while (index < source.length && source[index] != '\n') index++
                    tokens += Token(TokenType.COMMENT, source.substring(start, index), start, index)
                }

                c == '"' -> {
                    index++

                    while (index < source.length && source[index] != '"') {
                        if (source[index] == '\\' && index + 1 < source.length) {
                            index += 2
                        } else {
                            index++
                        }
                    }

                    if (index < source.length) {
                        index++
                    }

                    tokens += Token(
                        TokenType.STRING,
                        source.substring(start, index),
                        start,
                        index
                    )
                }


                c.isLetter() || c == '_' -> {
                    while (index < source.length &&
                        (source[index].isLetterOrDigit() || source[index] == '_')
                    ) index++

                    val word = source.substring(start, index)
                    val type = KotlinLanguage.resolve(word)
                    tokens += Token(type, word, start, index)
                }

                else -> {
                    index++
                    tokens += Token(TokenType.OPERATOR, c.toString(), start, index)
                }
            }
        }

        return tokens
    }
    private fun peek(expected: Char): Boolean =
        index + 1 < source.length && source[index + 1] == expected
}
