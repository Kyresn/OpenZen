# GUI KNOWLEDGE BASE

## OVERVIEW
This package contains three ClickGUI implementations, with `PanelClickGui` + `panel/` acting as the current composed screen.

## STRUCTURE
```
gui/
├── PanelClickGui.java   # Current screen shell and input routing
├── panel/               # Active widgets, overlays, and setting renderers
├── NewClickGui.java     # Alternate animated panel-based screen
├── newclickgui/         # Element hierarchy used by NewClickGui
├── OldClickGui.java     # Legacy draggable category screen
└── legacy/              # OldClickGui components and setting widgets
```

## WHERE TO LOOK
| Task | Location | Notes |
|------|----------|-------|
| Main ClickGUI lifecycle | `PanelClickGui.java` | Open/close animation, scaling, search bar, toast handling, top-level mouse/key routing |
| Active category selector | `panel/CategoryBar.java` | Category icon layout, hover state, current category selection |
| Active module list | `panel/ModuleListPanel.java` | Category/search results, scroll handling, module hover state, bind display |
| Active settings pane | `panel/SettingsPanel.java` | Module toggle, pane transitions, clipping, per-setting layout |
| Setting type dispatch | `panel/setting/SettingRendererRegistry.java` | Maps `Setting<?>` instances to renderer implementations |
| Concrete setting UIs | `panel/setting/*.java` | Boolean/mode/number/multiselect behavior and click logic |
| Profile / scale popup flow | `panel/ProfileWidget.java`, `panel/SettingsPopup.java`, `panel/ScaleSwitchOverlay.java` | User info popup and delayed scale switching UX |
| Legacy fallback UI | `OldClickGui.java`, `legacy/` | Draggable category columns with `ModuleButton` trees |
| Alternate experimental UI | `NewClickGui.java`, `newclickgui/` | `UIElement`/`ModuleElement` hierarchy with per-panel animations |

## CONVENTIONS
- Treat `PanelClickGui` as the default integration target unless the task explicitly names `OldClickGui` or `NewClickGui`.
- Preserve the composition pattern: screen shell in `PanelClickGui`, feature-specific behavior in `panel/*`, setting-specific behavior in `panel/setting/*`.
- Match the existing rendering stack: `GuiGraphics` entrypoints, `Renderer`/`DrawContext` helpers, `GlHelper` text drawing, and `RenderUtil` rounded primitives.
- Respect scale-aware math. Active widgets derive most dimensions from the `scale` argument and must keep scroll offsets in sync when scale changes.
- Keep category and module ordering aligned with `Category.values()` and module-manager queries instead of hardcoding lists.
- When adding a new setting type for the active ClickGUI, implement a `SettingRenderer` and register it in `SettingRendererRegistry`.

## ANTI-PATTERNS
- Do not mix the three GUI generations in one change path; edits to `panel/` should not silently depend on `legacy/` or `newclickgui/` classes.
- Do not bypass `PanelClickGui` child panels with duplicated hit-testing or duplicated state stored in multiple widgets.
- Do not add raw fixed-pixel coordinates to active panel widgets without considering the passed `scale` and clipping regions.
- Do not render setting controls directly inside `SettingsPanel` when the behavior belongs in a dedicated renderer class.
- Do not remove the config-save behavior on GUI close without replacing it in the same lifecycle path.
