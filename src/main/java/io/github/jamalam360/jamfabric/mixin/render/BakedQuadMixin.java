package io.github.jamalam360.jamfabric.mixin.render;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Jamalam360
 */

@Mixin(BakedQuad.class)
public class BakedQuadMixin {
    @Shadow @Final protected Sprite sprite;

    @Inject(
            method = "hasColor",
            at = @At("HEAD"),
            cancellable = true
    )
    public void jamfabric$forceColor(CallbackInfoReturnable<Boolean> cir) {
        if (sprite.getId().equals(new Identifier("jamfabric:block/jam_block"))) {
            cir.setReturnValue(true);
        }
    }
}
