package io.github.jamalam360.jamfabric.mixin.general;

import io.github.jamalam360.jamfabric.block.JamPotBlockEntity;
import io.github.jamalam360.jamfabric.util.registry.BlockRegistry;
import io.github.jamalam360.jamfabric.util.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Shadow @Final @Deprecated private Block block;

    @Inject(
            method = "useOnBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    public void jamfabric$useCakeOnPotCheck(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        BlockState jamfabric$state = context.getWorld().getBlockState(context.getBlockPos());
        if (jamfabric$state.isOf(BlockRegistry.JAM_POT)) {
            if (this.block instanceof CakeBlock) {
                JamPotBlockEntity jamfabric$entity = ((JamPotBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos()));
                if (jamfabric$entity != null && jamfabric$entity.canInsertIngredients()) {
                    jamfabric$entity.jam.add(ItemRegistry.FAKE_CAKE);
                    context.getStack().decrement(1);
                    cir.setReturnValue(ActionResult.CONSUME);
                }
            }
        }
    }
}
