# MiniKotlinIDE

A lightweight Kotlin script editor and runner with syntax highlighting and clickable error output, built with Kotlin Multiplatform and Compose Multiplatform for Desktop.

[![Demo Video](screenshots/video_preview.png)](screenshots/video.mp4)
---

## Key Features

- **Syntax highlighting**: Real-time tokenization for Kotlin keywords, strings, and comments.
- **Smart Navigation**: Clickable compiler error messages that jump directly to the line and column in the editor.
- **Dynamic Layout**: Resizable split-pane interface (Editor & Output) built with custom drag logic.
- **Reactive state management** using `ViewModel` + `StateFlow`
- **Process Control**: Track execution history, duration, and exit codes with the ability to terminate scripts instantly.
- **Smart editor behaviors**: auto-closing brackets, quotes, and indentation

---

## Architecture & Key Concepts

### ViewModel & StateFlow

All script logic is centralized in `ScriptViewModel.kt`. It manages editor text, script output, and execution status (`Idle`, `Running`, `Finished`, `Error`).  

- `runScript()` executes the Kotlin script in a coroutine on a background thread.
- `stopScript()` cancels running jobs and forcibly terminates processes.
- `_cursorPosition` tracks editor cursor and allows jumping to errors.

---

### Output Pane

The output pane displays script output with **clickable errors**:

- Parses errors via `ScriptParser` and links them to editor positions.
- Auto-scrolls to the bottom as new output arrives.
- Uses `AnnotatedString` and `LinkAnnotation.Clickable` for interactivity.

---

### Code Editor

`CodeEditor.kt` implements a fully functional editor with:

- Line numbers
- Syntax highlighting
- Cursor tracking
- Smart auto-pairing for `()`, `{}`, `[]`, `"`, `'`
- Automatic indentation on Enter and tab insertion

- Scrolls horizontally and vertically with the cursor.
- Updates reactive `StateFlow` in `ViewModel` on text changes.

---

### Script Parsing & Sanitization

`ScriptParser.kt` handles compiler output parsing:

- Converts compiler errors into `ScriptError` objects with line/column info.
- Sanitizes temporary file paths in output.
- Enables clickable error navigation in the editor.

---


## Engineering Features
### Reactive State & Concurrency Management
Unlike simple text editors, this IDE manages a complex lifecycle of external processes using Kotlin Coroutines and StateFlow.

ViewModel Architecture: Uses MutableStateFlow with an immutable ScriptUiState to ensure a "Single Source of Truth."

Dispatchers & Lifecycle: Script execution is offloaded to Dispatchers.IO to keep the UI thread (Main) buttery smooth even during heavy compilation.

Process Lifecycle Safety: Implemented NonCancellable blocks and destroyForcibly() to ensure no "zombie processes" are left behind if a user stops a script or closes the app.

### "Smart Link" Error Navigation
The IDE implements a custom bridge between the compiler output and the editor.

Regex Parsing: A specialized ScriptParser uses optimized regular expressions to extract line:column metadata from the compiler's stderr.

Interactive Output: The OutputPane uses LinkAnnotation.Clickable (Compose 1.7+ pattern) to turn raw text into interactive links that provide instant navigation back to the source code.

### Custom Logic & UI Optimizations
State-Driven Highlighting: The editor uses a custom-built Lexer that performs tokenization. Highlighting is applied via LaunchedEffect only when the text changes, preventing redundant re-renders.

Dynamic Layouts: Implemented a custom Split-Pane logic using BoxWithConstraints and manual pixel-to-ratio calculations to allow real-time resizing.

Automatic Bracket Pairing: Enhances Developer Experience (DX) by automatically inserting closing characters () {} [] "" with intelligent cursor placement.


## Screenshots

<p float="left">
  <img src="screenshots/correct.png" width="200" alt="Correct" />
  <img src="screenshots/running.png" width="200" alt="Running" />
</p>


---

## Getting Started

### Requirements

- JDK 17+
- Kotlin 2.3+
- Gradle 8+ (or included wrapper)
- Kotlin installed on your system path (kotlinc or kotlinc.bat)

### Run locally

Clone the repository:

```bash
git clone https://github.com/nikmasi/MiniKotlinIDE.git
cd MiniKotlinIDE\MiniKotlinIDE
```

Run via Gradle:

```bash
./gradlew run
```
