package io.github.jamalam360.jamfabric.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.foundationgames.mealapi.api.v0.PlayerFullnessUtil;
import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.JamNbtHelper;
import io.github.jamalam360.jamfabric.util.HungerManagerDuck;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Jamalam360
 */

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin implements HungerManagerDuck {
    @Shadow
    public abstract void add(int food, float saturationModifier);

    private PlayerEntity parent;

    @Inject(
            method = "eat",
            at = @At("HEAD"),
            cancellable = true
    )
    public void eatMixin(Item item, ItemStack stack, CallbackInfo ci) {
        if (stack.isOf(JamModInit.JAM_JAR)) {
            Pair<Integer, Float> pair = JamNbtHelper.getJamJarHungerAndSaturation(stack.getOrCreateNbt());

            if (!this.parent.world.isClient && pair.getFirst() > 0) {
                PlayerFullnessUtil.instance().addFullness((ServerPlayerEntity) this.parent, pair.getFirst());
            }

            ci.cancel();
        }
    }

    @Override
    public void setPlayer(PlayerEntity player) {
        this.parent = player;
    }
}
