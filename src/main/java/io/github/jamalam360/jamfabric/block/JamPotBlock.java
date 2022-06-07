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

package io.github.jamalam360.jamfabric.block;

import io.github.jamalam360.jamfabric.data.JamIngredient;
import io.github.jamalam360.jamfabric.data.JamIngredientRegistry;
import io.github.jamalam360.jamfabric.registry.ItemRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * @author Jamalam360
 */
public class JamPotBlock extends BlockWithEntity {
    private static final Random RANDOM = new Random();

    public JamPotBlock() {
        super(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        JamPotBlockEntity blockEntity = (JamPotBlockEntity) world.getBlockEntity(pos);

        if (blockEntity == null) return super.onUse(state, world, pos, player, hand, hit);

        if (stack.isIn(ConventionalItemTags.WATER_BUCKETS) && blockEntity.canInsertWater()) {
            blockEntity.setFilledWater(true);

            if (!player.isCreative()) {
                stack.decrement(1);

                if (stack.getItem().getRecipeRemainder() != null) {
                    player.giveItemStack(stack.getItem().getRecipeRemainder().getDefaultStack());
                } else {
                    player.giveItemStack(Items.BUCKET.getDefaultStack());
                }
            }

            if (world.isClient) {
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            return ActionResult.SUCCESS;
        } else if (stack.isOf(Items.SUGAR) && blockEntity.canInsertSugar()) {
            blockEntity.setFilledSugar(true);

            if (!player.isCreative()) {
                stack.decrement(1);
            }

            playRandomBrewingSound(world, pos);

            return ActionResult.SUCCESS;
        } else if (stack.isFood() && blockEntity.canInsertIngredients() && !stack.isOf(ItemRegistry.JAM_JAR)) {
            blockEntity.jam.add(stack.getItem());

            if (!player.isCreative()) {
                stack.decrement(1);
            }

            playRandomBrewingSound(world, pos);

            return ActionResult.SUCCESS;
        } else if (JamIngredientRegistry.has(stack.getItem()) && blockEntity.canInsertIngredients()) {
            JamIngredient ingredient = JamIngredientRegistry.get(stack.getItem());
            blockEntity.jam.addRaw(ingredient);

            if (!player.isCreative()) {
                stack.decrement(1);
            }

            playRandomBrewingSound(world, pos);

            return ActionResult.SUCCESS;
        } else if (stack.isEmpty() && player.isSneaking() && !world.isClient) {
            if (blockEntity.jam.getIngredients().size() > 0) {
                player.giveItemStack(new ItemStack(blockEntity.jam.removeLast()));
                playRandomBrewingSound(world, pos);

                return ActionResult.SUCCESS;
            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
        if (world.isClient) {
            BlockEntity entity = world.getBlockEntity(pos);

            if (entity instanceof JamPotBlockEntity jamPotBlockEntity && jamPotBlockEntity.hasWater() && jamPotBlockEntity.hasSugar()) {
                if (random.nextDouble() < 0.035) {
                    playRandomBrewingSound(world, pos);
                }

                double d = (double) pos.getX() + 0.4 + (double) RANDOM.nextFloat() * 0.2;
                double e = (double) pos.getY() + 0.7 + (double) RANDOM.nextFloat() * 0.3;
                double f = (double) pos.getZ() + 0.4 + (double) RANDOM.nextFloat() * 0.2;
                world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new JamPotBlockEntity(pos, state);
    }

    public static void playRandomBrewingSound(World world, BlockPos pos) {
        if (world.isClient) {
            world.playSound(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    SoundEvents.BLOCK_BREWING_STAND_BREW,
                    SoundCategory.BLOCKS,
                    RANDOM.nextFloat(0.5f, 1f),
                    RANDOM.nextFloat(0.6f, 1.3f),
                    false
            );
        }
    }
}
