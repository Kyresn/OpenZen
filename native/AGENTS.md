# NATIVE INJECTOR KNOWLEDGE BASE

## OVERVIEW
C++17 Windows-only pipeline for process injection and JVMTI-based Java agent attachment.

## WHERE TO LOOK
| Component | Location | Responsibility |
|-----------|----------|----------------|
| Injection Logic | `loader/src/injector.cpp` | `CreateRemoteThread` and `LoadLibrary` orchestration |
| Process UI | `loader/src/ui.cpp` | Win32 GUI for target process selection |
| Resource Embedding | `loader/src/embedded_dll.cpp` | Extracting DLL/JAR from `RCDATA` segments |
| JVM Attachment | `dll/src/jvm_attach.cpp` | JVMTI entry point and `JNI_GetCreatedJavaVMs` |
| Class Loading | `dll/src/class_loader.cpp` | Manual class definition from memory-mapped JAR |
| Native Logging | `dll/src/diagnostics.cpp` | File-based logging to `%TEMP%\openzen.log` |

## CONVENTIONS
- **Toolchain**: Requires MSVC (Visual Studio 2022) and CMake.
- **Resource Pipeline**: The Java JAR is embedded into the DLL, which is then embedded into the Loader EXE.
- **Error Handling**: Native errors must be logged to the temp file since `stdout` is usually unavailable in the target process.
- **JNI/JVMTI**: Prefer standard JVMTI functions over internal JVM offsets for stability across Java versions.
- **Memory**: Use `std::vector<char>` for buffer management during resource extraction to ensure RAII.

## ANTI-PATTERNS
- **Standard Library Bloat**: Avoid heavy STL usage in `dll/` to keep the final EXE size manageable for distribution.
- **Blocking JVM Threads**: Never perform long-running operations in the `Agent_OnAttach` thread; spawn a detached thread if necessary.
- **Hardcoded Paths**: Do not use absolute paths for temporary files; always resolve via `GetTempPathW`.
- **Cross-Platform Abstractions**: Do not add `ifdef` blocks for Linux/macOS; this pipeline is architecturally bound to Windows Win32 APIs.
- **Raw Pointers**: Avoid raw `new`/`delete` for buffers; use smart pointers or stack allocation for resource extraction.
