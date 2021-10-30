package io.github.jamalam360.jamfabric.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * @author Jamalam360
 */
public class JamPotBlock extends BlockWithEntity {
    public JamPotBlock() {
        super(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        /*ItemStack stack = player.getStackInHand(hand);
        JamPotBlockEntity blockEntity = (JamPotBlockEntity) world.getBlockEntity(pos);

        if (stack.isFood() && stack.getItem() != ItemRegistry.JAM_JAR.item()) {
            List<Item> items = new ArrayList<>();

            for (int i = 0; i < stack.getCount(); i++) {
                items.add(stack.getItem());
            }

            blockEntity.setItems(items.toArray(new Item[0]));
            stack.setCount(0);

            return ActionResult.SUCCESS;
        } else if (stack.getItem() == Items.WATER_BUCKET) {
            blockEntity.hasWater = true;
            stack.setCount(0);
            player.setStackInHand(hand, new ItemStack(Items.BUCKET));
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);*/

        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.SUCCESS;
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

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
