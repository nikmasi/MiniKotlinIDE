package org.example.project.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.project.model.RunInfo
import org.example.project.model.ScriptStatus
import org.example.project.model.ScriptUiState
import org.example.project.parser.ScriptParser
import java.io.File

class ScriptViewModel : ViewModel() {
    private var currentProcess: Process? = null
    private var currentJob: Job? = null

    private val _uiState = MutableStateFlow(ScriptUiState())
    val uiState: StateFlow<ScriptUiState> = _uiState.asStateFlow()

    private val outputBuffer = StringBuilder()

    var runHistory = mutableStateListOf<RunInfo>()
        private set

    fun updateScriptText(text: String) {
        _uiState.value = _uiState.value.copy(scriptText = text)
    }

    fun runScript() {
        stopScript()

        currentJob = viewModelScope.launch(Dispatchers.IO) {
            outputBuffer.setLength(0)
            val currentRun = RunInfo(startTime = System.currentTimeMillis())
            runHistory.add(0, currentRun)

            _uiState.value = _uiState.value.copy(
                status = ScriptStatus.Running,
                output = "Starting scripting runtime...\n"
            )

            try {
                val tempFile = File.createTempFile("script", ".kts").apply {
                    deleteOnExit()
                    writeText(_uiState.value.scriptText)
                }

                val process = ProcessBuilder(getKotlinCommand(), "-script", tempFile.absolutePath)
                    .redirectErrorStream(true)
                    .start()

                currentProcess = process

                process.inputStream.bufferedReader().use { reader ->
                    var line: String?
                    while (isActive) {
                        line = reader.readLine() ?: break

                        val sanitized = ScriptParser.sanitizeLine(line)
                        outputBuffer.append(sanitized).append("\n")

                        _uiState.value = _uiState.value.copy(output = outputBuffer.toString())
                    }
                }

                val exitCode = if (process.isAlive) {
                    process.destroyForcibly()
                    -1
                } else {
                    process.waitFor()
                }

                if (isActive) {
                    _uiState.value = _uiState.value.copy(
                        status = ScriptStatus.Finished(exitCode),
                        output = outputBuffer.toString() + "\nProcess Finished"
                    )
                }

            } catch (e: Exception) {
                if (e !is CancellationException) {
                    _uiState.value = _uiState.value.copy(
                        status = ScriptStatus.Error(e.localizedMessage ?: "Unknown error"),
                        output = outputBuffer.toString() + "\n[Error] ${e.localizedMessage}"
                    )
                }
            } finally {
                val wasCancelled = !isActive
                val finishTime = System.currentTimeMillis()

                currentProcess?.destroyForcibly()
                currentProcess = null

                val index = runHistory.indexOf(currentRun)
                if (index != -1) {
                    runHistory[index] = currentRun.copy(
                        endTime = finishTime,
                        exitCode = if (wasCancelled) -130 else runHistory[index].exitCode
                    )
                }

                if (wasCancelled) {
                    withContext(NonCancellable) {
                        _uiState.value = _uiState.value.copy(
                            status = ScriptStatus.Idle,
                            output = outputBuffer.toString() + "\n[Process Terminated]\n"
                        )
                    }
                }
            }
        }
    }
    fun stopScript() {
        currentJob?.cancel()
        currentProcess?.let { process ->
            process.descendants().forEach { it.destroyForcibly() }
            process.destroyForcibly()
        }
        currentProcess = null
        currentJob = null

        _uiState.value = _uiState.value.copy(
            status = ScriptStatus.Idle,
            output  = _uiState.value.output + "\n[Process Terminated]\n"
        )
    }

    private fun getKotlinCommand(): String {
        val os = System.getProperty("os.name").lowercase()
        return if (os.contains("win")) "kotlinc.bat" else "kotlinc"
    }

    private val _cursorPosition = MutableStateFlow<Pair<Int, Int>?>(null)
    val cursorPosition: StateFlow<Pair<Int, Int>?> = _cursorPosition.asStateFlow()

    fun moveCursorTo(line: Int, column: Int) {
        _cursorPosition.value = line to column
    }
}
