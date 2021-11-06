package io.github.jamalam360.jamfabric.mixin;

import com.mojang.authlib.GameProfile;
import io.github.jamalam360.jamfabric.util.HungerManagerDuck;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Jamalam360
 */

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow protected HungerManager hungerManager;

    @Inject(
            at = @At("TAIL"),
            method = "<init>"
    )
    public void jamFabric_initMixin(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci) {
        ((HungerManagerDuck) this.hungerManager).setPlayer((PlayerEntity) (Object) this);
    }
}
