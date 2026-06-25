package shit.zen.patch;

import asm.patchify.annotation.At;
import asm.patchify.annotation.Inject;
import asm.patchify.annotation.Patch;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import shit.zen.modules.impl.render.XRay;

/**
 * Compatibility patch for Embeddium/Sodium's {@code BlockOcclusionCache}.
 *
 * <p>Sodium/Embeddium replaces vanilla's chunk mesh building with its own pipeline. The
 * vanilla {@link Block#shouldRenderFace()} method — which our {@link BlockPatch} hooks
 * — is never called by Embeddium's {@code BlockRenderer}. Instead, face visibility is
 * decided by {@code BlockOcclusionCache.shouldDrawSide()}, which has its own entirely
 * independent occlusion logic.</p>
 *
 * <p>This patch injects into {@code BlockOcclusionCache.shouldDrawSide()} so that when
 * XRay is enabled the result is forced to match {@link XRay#isXrayVisible(Block)},
 * exactly like our vanilla patch.</p>
 *
 * <p>The target class is referenced by {@link Patch#className()} rather than by
 * {@link Patch#value()} because Embeddium is an optional mod — the class is not
 * available at compile time. The patch is registered <em>unconditionally</em> in
 * {@link shit.zen.ZenClient#registerPatches()} (we must <em>not</em> probe with
 * {@link Class#forName(String)}, which would force the target class to load before our
 * transformer is installed and thus defeat the patch). When Embeddium is absent the
 * target class simply never loads, so the registered patch is harmless. The method
 * descriptor is left empty (name-only match) because the parameter types in Embeddium's
 * bytecode may differ between Yarn and Mojmap mappings depending on the Embeddium build
 * and compatibility layer.</p>
 */
@Patch(className = "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache")
public class BlockOcclusionCachePatch {

    /**
     * Injected at HEAD of {@code shouldDrawSide}. When XRay is enabled we cancel the
     * original method and force the result: target blocks return {@code true} (all
     * faces visible = rendered through walls) and non-target blocks return
     * {@code false} (no faces visible = completely transparent).
     *
     * <p>All reference parameters are declared as {@link Object} rather than their actual
     * types because Embeddium may be compiled with either Yarn or Mojmap mappings — every
     * argument of {@code shouldDrawSide} is a reference type, so declaring them as
     * {@code Object} lets the forwarded values widen cleanly regardless of the concrete
     * runtime types. Note this only protects against <em>type</em> mismatches: the
     * parameter <em>count</em> must still equal the target method's arity (receiver + args),
     * otherwise the generated call site is invalid. {@code PatchTransformer.injectHead}
     * now guards this and skips the injection with a warning rather than emitting bytecode
     * that would fail verification. The only param we actually read is {@code selfState},
     * which is cast to Mojmap {@link BlockState} (always correct at runtime since the loaded
     * Minecraft classes are Mojmap-mapped in a Forge environment).</p>
     *
     * @param self       the {@code BlockOcclusionCache} instance (unused)
     * @param selfState  the {@code BlockState} of the block being rendered
     * @param view       the {@code BlockView/BlockGetter} (unused)
     * @param pos        the {@code BlockPos} (unused)
     * @param direction  the facing {@code Direction} (unused)
     * @param ci         callback info — cancelled with redirected result
     */
    @Inject(
            method = "shouldDrawSide",
            desc = "",
            at = @At(At.Type.HEAD)
    )
    public static void onShouldDrawSide(Object self, Object selfState, Object view,
                                         Object pos, Object direction, CallbackInfo ci) {
        XRay xray = XRay.INSTANCE;
        if (xray == null || !xray.isEnabled()) {
            return; // XRay off -> keep original Embeddium occlusion logic
        }
        // Cast to Mojmap BlockState — always correct at runtime on Forge
        boolean visible = xray.isXrayVisible(((BlockState) selfState).getBlock());
        ci.result = visible ? Boolean.TRUE : Boolean.FALSE;
        ci.cancel();
    }
}
