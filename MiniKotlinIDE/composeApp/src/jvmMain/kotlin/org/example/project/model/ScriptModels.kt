package org.example.project.model

import androidx.compose.runtime.Immutable
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Immutable
data class ScriptUiState(
    val scriptText: String = "",
    val output: String = "",
    val status: ScriptStatus = ScriptStatus.Idle
)

sealed interface ScriptStatus {
    object Idle : ScriptStatus
    object Running : ScriptStatus
    data class Finished(val exitCode: Int) : ScriptStatus
    data class Error(val message: String) : ScriptStatus
}

@Immutable
data class ScriptError(
    val line: Int,
    val column: Int,
    val message: String,
    val rawText: String
)

@Immutable
data class RunInfo(
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val exitCode: Int? = null
) {
    val duration: String
        get() {
            if (endTime == null) return "-"
            val diff = (endTime - startTime).toDuration(DurationUnit.MILLISECONDS)
            return diff.toString()
        }

    val isSuccess: Boolean get() = exitCode == 0
}
