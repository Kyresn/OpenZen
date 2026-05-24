# PATCH KNOWLEDGE BASE

## OVERVIEW
ASM patch layer that injects, wraps, or rewrites Minecraft client methods to bridge runtime engine calls into Zen events, rendering hooks, and behavioral overrides.

## WHERE TO LOOK
| Task | Location | Notes |
|------|----------|-------|
| Bootstrap and lifecycle hooks | `MinecraftPatch.java` | Initializes `ZenClient`, removes the mod from Forge lists, fires tick/motion/disconnect hooks |
| Player motion and movement rewrites | `LocalPlayerPatch.java` | Most invasive bytecode edits; builds `MotionEvent` and rewires getters to event fields |
| Packet interception | `ConnectionPatch.java` | Early-return transforms for inbound/outbound packet cancellation |
| 2D/GL render entry points | `GameRendererPatch.java` | HUD render, GL render callback, FOV/projection changes, hurt-cam suppression |
| Entity and model state hooks | `EntityPatch.java`, `LivingEntityPatch.java`, `HumanoidModelPatch.java` | Event-driven state edits for movement, animation, or combat-sensitive logic |
| Item/use interaction hooks | `ItemPatch.java`, `ItemInHandRendererPatch.java`, `ItemInHandLayerPatch.java`, `FriendlyByteBufPatch.java` | Use-item behavior, render manipulation, and packet/buffer side effects |
| Utility cancellation carrier | `CallbackInfo.java` | Minimal local callback object used by `@Inject` handlers to cancel patched execution |

## CONVENTIONS
- One patch class usually targets one runtime class and is named `<Target>Patch` with `@Patch(Target.class)`.
- Prefer `@Inject`/`@WrapInvoke` when a stable call site exists; use `@Transform` only when annotations cannot express the required control flow.
- `@Transform` code should resolve Mojang names through `ReflectionUtil.getMappedMethodName(...)` instead of hardcoding mapped method names.
- Cancellation paths use `CallbackInfo.cancel()` or explicit inserted `RETURN` instructions; preserve the original method contract when aborting execution.
- Guard gameplay/event dispatch with `ZenClient.isReady()` and null-sensitive checks when the world, player, or module manager may not exist yet.
- Keep ASM edits local and surgical: insert around a single anchor, store any new locals deliberately, and account for shifted var indexes after injection.
- Static helper methods inside the patch class are the normal bridge from patched bytecode back into higher-level event code.

## ANTI-PATTERNS
- Do not add broad `@Overwrite` patches when a narrower injection or invoke-wrap can preserve more vanilla behavior.
- Do not hardcode unobfuscated method names inside raw ASM transforms; mapping drift will silently break anchor detection.
- Do not mutate instruction lists without checking surrounding node shape first; many transforms here assume a specific preceding `VarInsnNode` or following jump.
- Do not fire Zen events before readiness checks or during bootstrap-only code paths unless the patch is explicitly responsible for initialization.
- Do not cancel network, motion, or render methods without confirming the caller still leaves Minecraft in a valid state after the early return.
- Do not mix unrelated module behavior into a single patch helper; keep each injection tied to a specific engine hook so failures stay debuggable.
