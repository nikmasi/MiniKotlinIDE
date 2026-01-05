# MiniKotlinIDE

A lightweight Kotlin script editor and runner with syntax highlighting and clickable error output, built with Kotlin Multiplatform and Compose Multiplatform for Desktop.

[![Demo Video](screenshots/video_preview.png)](screenshots/video.mp4)

---

## Key Features

- **Kotlin syntax highlighting** in editor pane
- **Clickable error messages** with cursor navigation
- **Resizable editor & output panes**
- **Reactive state management** using `ViewModel` + `StateFlow`
- **Script execution** through temporary `.kts` files
- **Run history tracking** with start/end times and exit codes
- **Smart editor behaviors**: auto-closing brackets, quotes, and indentation

---

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


## Screenshots

<p float="left">
  <img src="screenshots/correct.png" width="400" alt="Correct" />
  <img src="screenshots/running.png" width="400" alt="Running" />
</p>

![stop](https://github.com/user-attachments/assets/88088e15-2bf1-4a62-9424-1ad280cd1f56)

![run](https://github.com/user-attachments/assets/e5dc81b3-5e51-4e21-a214-7b10c3362754)

![error](https://github.com/user-attachments/assets/d4a59f55-df63-4c8d-9c8c-52bbee34d41f)



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
cd MiniKotlinIDE
```

Run via Gradle:

```bash
./gradlew run
```
