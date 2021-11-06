package io.github.jamalam360.jamfabric.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.JamNbtHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * @author Jamalam360
 */

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Redirect(
            method = "applyFoodEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/FoodComponent;getStatusEffects()Ljava/util/List;"
            )
    )
    public List<Pair<StatusEffectInstance, Float>> jamfabric_applyFoodEffects(FoodComponent instance, ItemStack stack, World world, LivingEntity targetEntity) {
        if (stack.isOf(JamModInit.JAM_JAR)) {
            return JamNbtHelper.getJamJarEffects(stack.getOrCreateNbt());
        } else {
            return instance.getStatusEffects();
        }
    }

    @Redirect(
            method = "eatFood",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
            )
    )
    public void jamfabric_eatFoodDecrementRedirect(ItemStack stack, int amount) {
        if (stack.isOf(JamModInit.JAM_JAR)) {
            JamNbtHelper.yeetItems(stack.getOrCreateNbt(), "Ingredients");
        } else {
            stack.decrement(amount);
        }
    }
}
