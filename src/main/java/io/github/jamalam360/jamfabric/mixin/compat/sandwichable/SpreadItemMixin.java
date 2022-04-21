package io.github.jamalam360.jamfabric.mixin.compat.sandwichable;

import io.github.foundationgames.sandwichable.items.SpreadItem;
import io.github.foundationgames.sandwichable.items.spread.SpreadType;
import io.github.jamalam360.jamfabric.compat.sandwichable.JamSpreadType;
import io.github.jamalam360.jamfabric.util.Jam;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Jamalam360
 */
@Mixin(SpreadItem.class)
public abstract class SpreadItemMixin {
    @Unique
    private ItemStack jamfabric$capturedStack;

    @Inject(
            method = "finishUsing",
            at = @At("HEAD")
    )
    public void jamfabric$captureItemStack(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        this.jamfabric$capturedStack = stack;
    }

    @Redirect(
            method = "finishUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/github/foundationgames/sandwichable/items/spread/SpreadType;getHunger()I"
            ),
            remap = false
    )
    public int jamfabric$getHungerRedirect(SpreadType instance) {
        if (instance instanceof JamSpreadType) {
            return Jam.fromNbt(jamfabric$capturedStack.getSubNbt("Jam")).hunger();
        } else {
            return instance.getHunger();
        }
    }

    @Redirect(
            method = "finishUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/github/foundationgames/sandwichable/items/spread/SpreadType;getSaturationModifier()F"
            ),
            remap = false
    )
    public float jamfabric$getSaturationModifierRedirect(SpreadType instance) {
        if (instance instanceof JamSpreadType) {
            return Jam.fromNbt(jamfabric$capturedStack.getSubNbt("Jam")).saturation();
        } else {
            return instance.getSaturationModifier();
        }
    }
}
