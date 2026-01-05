package org.example.project.parser

import org.example.project.model.ScriptError

object ScriptParser {
    private val ERROR_REGEX = Regex(
        pattern = """^.*\.kts:(\d+):(\d+):\s*error:\s*(.*)$""",
        options = setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
    )

    fun parseErrors(output: String): List<ScriptError> {
        if (output.isBlank()) return emptyList()

        return output.lineSequence()
            .mapNotNull { line ->
                ERROR_REGEX.find(line)?.let { match ->
                    val (lineNum, colNum, msg) = match.destructured
                    ScriptError(
                        line = lineNum.toInt(),
                        column = colNum.toInt(),
                        message = msg.trim(),
                        rawText = line
                    )
                }
            }
            .toList()
    }
    fun sanitizeLine(line: String): String {
        return line.replace(Regex("""^.*[\\/](script\d*\.kts)"""), "script.kts")
    }
}