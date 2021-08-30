package io.github.jamalam360.jamfabric.block;

import io.github.jamalam360.jamfabric.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

/**
 * @author Jamalam360
 */
public class JamPotBlockEntity extends BlockEntity {
    public JamPotBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.JAM_POT.getBlockEntityType(), pos, state);
    }
}
