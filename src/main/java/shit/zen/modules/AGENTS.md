# MODULES KNOWLEDGE BASE

## OVERVIEW
Core cheat logic implementation layer containing all toggleable features and their settings.

## STRUCTURE
- `impl/combat/`: Combat-related cheats like KillAura and AntiKB.
- `impl/movement/`: Movement cheats like Fly, Scaffold, and Speed.
- `impl/player/`: Player-state cheats like NoFall and AutoWeb.
- `impl/render/`: Visual cheats like ESP and NameTags.
- `impl/world/`: World-interaction cheats like AutoTools and Teams.
- `impl/exploit/`: Server-side exploit modules.
- `impl/misc/`: Miscellaneous utility modules.

## WHERE TO LOOK
- `Module.java`: Base class defining lifecycle methods like onEnable and onDisable.
- `shit.zen.manager.ModuleManager`: Central registry and event dispatcher for all modules.
- `impl/combat/KillAura.java`: Reference for complex module implementation with settings and rotations.
- `impl/movement/Scaffold.java`: Reference for complex world interaction and placement logic.

## CONVENTIONS
- Settings must be declared as public final fields.
- Settings are registered via reflection in the registerSettings method.
- Use the @EventTarget annotation for event handling within modules.
- Modules must call the super constructor with a name and category.
- Use the INSTANCE pattern for singleton access to module state.
- Check ZenClient.isReady before performing world or player actions.

## ANTI-PATTERNS
- Do not manually add settings to the settings list. Use the reflection-based registration.
- Avoid raw packet sending without checking the client ready state.
- Do not hardcode keybinds in implementation files. Use the Module constructor.
- Avoid complex inheritance within the impl directory. Use utility classes instead.
- Do not modify Minecraft state without restoring it in onDisable.
