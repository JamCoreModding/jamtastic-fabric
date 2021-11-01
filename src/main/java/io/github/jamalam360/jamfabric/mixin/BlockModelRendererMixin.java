package io.github.jamalam360.jamfabric.mixin;

import io.github.jamalam360.jamfabric.block.JamPotBlockEntityRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Jamalam360
 */

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {
    @Unique
    private static boolean forceColor = false;

    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/block/BlockState;Lnet/minecraft/client/render/model/BakedModel;FFFII)V",
            at = @At(
                 value = "INVOKE",
                 target = "Lnet/minecraft/client/render/block/BlockModelRenderer;renderQuad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;FFFLjava/util/List;II)V",
                 ordinal = 0,
                 shift = At.Shift.BEFORE
            )
    )
    public void renderMixin(MatrixStack.Entry entry, VertexConsumer vertexConsumer, BlockState blockState, BakedModel bakedModel, float f, float g, float h, int i, int j, CallbackInfo ci) {
        forceColor = blockState == JamPotBlockEntityRenderer.JAM;
    }

    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/block/BlockState;Lnet/minecraft/client/render/model/BakedModel;FFFII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockModelRenderer;renderQuad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;FFFLjava/util/List;II)V",
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            )
    )
    public void renderMixin2(MatrixStack.Entry entry, VertexConsumer vertexConsumer, BlockState blockState, BakedModel bakedModel, float f, float g, float h, int i, int j, CallbackInfo ci) {
        forceColor = blockState == JamPotBlockEntityRenderer.JAM;
    }

    @Redirect(
            method = "renderQuad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;FFFLjava/util/List;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/BakedQuad;hasColor()Z"
            )
    )
    private static boolean redirectHasColor(BakedQuad instance) {
        if (forceColor) {
            forceColor = false;
            return true;
        } else {
            return instance.hasColor();
        }
    }
}
