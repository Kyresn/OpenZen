package shit.zen.patch;

import asm.patchify.annotation.At;
import asm.patchify.annotation.Inject;
import asm.patchify.annotation.Patch;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import shit.zen.ZenClient;
import shit.zen.modules.impl.render.XRay;

@Patch(BlockBehaviour.BlockStateBase.class)
public class BlockStateBasePatch {
    @Inject(
            method = "getRenderShape",
            desc = "()Lnet/minecraft/world/level/block/RenderShape;",
            at = @At(At.Type.HEAD)
    )
    public static void onGetRenderShape(BlockBehaviour.BlockStateBase state, CallbackInfo callbackInfo) {
        if (ZenClient.isReady() && XRay.INSTANCE != null
                && XRay.INSTANCE.isEnabled()
                && !XRay.INSTANCE.isTarget(state.getBlock())) {
            callbackInfo.cancel();
            callbackInfo.result = RenderShape.INVISIBLE;
        }
    }

    @Inject(
            method = "getShadeBrightness",
            desc = "(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F",
            at = @At(At.Type.HEAD)
    )
    public static void onGetShadeBrightness(
            BlockBehaviour.BlockStateBase state,
            BlockGetter blockGetter,
            BlockPos blockPos,
            CallbackInfo callbackInfo) {
        if (ZenClient.isReady() && XRay.INSTANCE != null
                && XRay.INSTANCE.isEnabled()
                && XRay.INSTANCE.isTarget(state.getBlock())) {
            callbackInfo.cancel();
            callbackInfo.result = 1.0F;
        }
    }

    @Inject(
            method = "propagatesSkylightDown",
            desc = "(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z",
            at = @At(At.Type.HEAD)
    )
    public static void onPropagatesSkylightDown(
            BlockBehaviour.BlockStateBase state,
            BlockGetter blockGetter,
            BlockPos blockPos,
            CallbackInfo callbackInfo) {
        if (ZenClient.isReady() && XRay.INSTANCE != null && XRay.INSTANCE.isEnabled()) {
            callbackInfo.cancel();
            callbackInfo.result = true;
        }
    }

    @Inject(
            method = "getLightBlock",
            desc = "(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)I",
            at = @At(At.Type.HEAD)
    )
    public static void onGetLightBlock(
            BlockBehaviour.BlockStateBase state,
            BlockGetter blockGetter,
            BlockPos blockPos,
            CallbackInfo callbackInfo) {
        if (ZenClient.isReady() && XRay.INSTANCE != null && XRay.INSTANCE.isEnabled()) {
            callbackInfo.cancel();
            callbackInfo.result = 0;
        }
    }
}
