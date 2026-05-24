# PROJECT KNOWLEDGE BASE

**Generated:** 2026-05-24

## OVERVIEW
OpenZen is a heavily customized Forge mod (Minecraft 1.20.1 / Forge 47.4.20) functioning as a cheat client. It features a dual-artifact architecture, combining a standard Java Forge mod JAR with an embedded Windows-only C++ native DLL/EXE injector pipeline.

## STRUCTURE
```
./
├── native/    # C++ native injector (loader EXE + injected DLL pipeline)
├── mapping/   # Mapping/artifact storage for deobfuscation
├── paste/     # Vendor/reference analysis content
├── run/       # Custom run artifact/configuration directory
└── src/main/java/shit/zen/ZenClient.java
```

## WHERE TO LOOK
| Task | Location | Notes |
|------|----------|-------|
| Core Initialization | `src/main/java/shit/zen/ZenClient.java` | Main client entry, manager setup |
| Module Logic (Cheats) | `src/main/java/shit/zen/modules/impl/` | Deeply nested domain subfolders |
| Bytecode Patches | `src/main/java/shit/zen/patch/` | ASM/patchify hook injections |
| Rendering Engine | `src/main/java/shit/zen/render/` | Custom `DrawContext`, shaders |
| GUI & ClickGUI | `src/main/java/shit/zen/gui/` | `PanelClickGui` and components |
| HUD Overlays | `src/main/java/shit/zen/hud/` | Draggable screen elements |
| Native Injector | `native/dll/` & `native/loader/` | C++ interop and JVM attach |
| Build & Tasks | `build.gradle` | Custom packaging/native targets |

## CONVENTIONS
- **Build**: Uses Java 17. Custom Gradle pipeline invokes CMake/MSVC for the native injector.
- **Agent Loading**: `runClient0` builds the JAR and launches the game with `-javaagent` to trigger `PatchAgent` for ASM bytecode retransformation.
- **Testing**: No conventional `src/test` JUnit suite. If tests are added, they must use Forge GameTest (`gameTestServer`).
- **Module Architecture**: Uses deep leaf packages (`modules.impl.<domain>.<subdomain>.impl`). Abstract bases live one level above implementation. Settings are registered via reflection.
- **Rendering**: Avoid raw `GuiGraphics` when possible. Wrap into `DrawContext` and use `RoundedRectShader`/`BlurRenderer`.

## ANTI-PATTERNS (THIS PROJECT)
- **Adding CI Workflows**: Do not blindly add standard Java CI workflows. The build relies heavily on a local Windows MSVC/CMake toolchain.
- **Hardcoding GL State**: Do not alter raw GL state without restoring it; prefer using the `Renderer` session wrapper and `DrawContext`.
- **Inheritance-Heavy Features**: Do not use deep inheritance frameworks. Behavior is assembled via event bus (`event/impl/`), registries (`ModuleManager`), and utilities.

## COMMANDS
```bash
# Build JAR and launch client with ASM javaagent
./gradlew runClient0

# Run native DLL pipeline
./gradlew stageNativeJar configureNative buildNative packageDist dll
```

## NOTES
- The mod metadata in `gradle.properties` might be template defaults; actual values are often overridden at runtime.
- High complexity hotspots include `AutoWebPlace.java`, `ChestStealer.java`, and `PatchTransformer.java`. Edit these with extreme caution.