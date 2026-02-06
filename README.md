# MiniKotlinIDE

A lightweight Kotlin script editor and runner with syntax highlighting and clickable error output, built with Kotlin Multiplatform for Desktop.

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
  <img src="screenshots/correct.png" width="200" alt="Correct" />
  <img src="screenshots/running.png" width="200" alt="Running" />
</p>

![run](https://github.com/user-attachments/assets/05ccd16c-f805-479f-885d-c3561dd7c77f)

![stop](https://github.com/user-attachments/assets/c16fcd42-2870-45c4-83ca-309436eb3274)

![error](https://github.com/user-attachments/assets/f37ac0a5-69e5-4800-b850-5af1ad03b099)

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
