/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jamalam360
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.jamfabric.item;

import io.github.jamalam360.jamfabric.block.JamPotBlockEntity;
import io.github.jamalam360.jamfabric.jam.Jam;
import io.github.jamalam360.jamfabric.jam.JamNameGenerator;
import io.github.jamalam360.jamfabric.registry.BlockRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Jamalam360
 */
public class JamJarItem extends Item {
    public JamJarItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockState(context.getBlockPos()).isOf(BlockRegistry.JAM_POT)) {
            JamPotBlockEntity blockEntity = (JamPotBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos());

            if (
                    blockEntity == null ||
                            context.getPlayer() == null ||
                            blockEntity.jam.getIngredients().size() == 0
                            || (context.getStack().getSubNbt("Jam") != null
                            && Jam.fromNbt(context.getStack().getSubNbt("Jam")).getIngredients().size() > 0)
            ) {
                return super.useOnBlock(context);
            }

            if (context.getPlayer().isSneaking()) {
                context.getStack().setSubNbt("Jam", blockEntity.jam.toNbt());

                if (context.getWorld().isClient) {
                    context.getPlayer().playSound(SoundEvents.BLOCK_BREWING_STAND_BREW, 1.0F, 1.0F);
                }

                if (!context.getWorld().isClient) {
                    context.getStack().setCustomName(new LiteralText(String.join(" ", JamNameGenerator.create(context.getStack().getSubNbt("Jam"), context.getPlayer()))));
                }

                blockEntity.empty();
                return ActionResult.SUCCESS;
            }
        }

        return super.useOnBlock(context);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        if (Jam.fromNbt(stack.getSubNbt("Jam")).getIngredients().size() == 0) {
            return 0;
        } else {
            return super.getMaxUseTime(stack);
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.addAll(Jam.fromNbt(stack.getSubNbt("Jam")).getTooltipContent());
    }
}
