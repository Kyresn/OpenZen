# RENDERING ENGINE KNOWLEDGE BASE

## OVERVIEW
Core graphics layer providing custom shaders, font rendering, and a high level DrawContext for UI components.

## WHERE TO LOOK
* `DrawContext.java`: Main API for UI and world space rendering.
* `Renderer.java`: Low level state management and primitive batching.
* `FontRenderer.java`: Custom glyph caching and text layout.
* `RoundedRectShader.java`: Antialiased rounded rectangle implementation.
* `BlurRenderer.java`: FBO based Gaussian blur and shadow effects.
* `Path.java`: Vector path definitions for complex geometry.
* `Paint.java`: Encapsulation of colors, gradients, and strokes.
* `GlHelper.java`: Common GL operations and stencil management.
* `CustomFont.java`: Implementation of the custom font engine.
* `StencilHelper.java`: Utility for managing the GL stencil buffer.

## CONVENTIONS
* **Context Usage**: Always pass `DrawContext` through UI component hierarchies.
* **Shader Lifecycle**: Factory classes handle initialization. Manual GLSL compilation is forbidden.
* **State Safety**: `StencilHelper` manages masking to ensure the buffer clears correctly.
* **Font Presets**: Access fonts via `Fonts.java` or `FontPresets.java` rather than loading files.
* **Precision**: Coordinate systems require float values for smooth animations.
* **Color Management**: Use the `Paint` class for all color definitions to support gradients.
* **Resource Loading**: Wrap textures in `ResourceLocationWrapper` to handle dynamic reloading.
* **Batching**: Group similar draw calls together to minimize state changes.
* **Unit Scaling**: Use the `Renderer` scale methods instead of manual matrix scaling.

## ANTI PATTERNS
* **Raw GL Calls**: RenderSystem or GlStateManager should be avoided outside of Renderer or GlHelper.
* **Direct FBO Access**: Prefer `BlurFbo` or `Texture` wrappers over raw integer handles.
* **Standard Fonts**: Minecraft's `Font` or `FontStorage` shouldn't be used for client specific UI.
* **Matrix Leaks**: Transformations must use try with resources or explicit pop calls.
* **Hardcoded Colors**: Hex literals should be avoided. Reference the theme system.
* **Redundant Binding**: Don't rebind textures or shaders if the state remains unchanged.
* **Raw FBO Binding**: Manual FBO binding often leads to state corruption. Use `BlurFbo`.
* **Immediate Mode**: Avoid using `Tessellator` directly. Use the `Renderer` batching API.
