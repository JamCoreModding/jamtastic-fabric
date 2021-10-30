package io.github.jamalam360.jamfabric.block;

import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.NbtHelper;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamalam360
 */
public class JamPotBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private final List<Item> items = new ArrayList<>();
    public boolean hasWater = false;
    public static final int CAPACITY = 4;

    public JamPotBlockEntity(BlockPos pos, BlockState state) {
        super(JamModInit.JAM_POT_BLOCK_ENTITY, pos, state);
    }

    public void setItems(Item... newItems) {
        items.addAll(List.of(newItems));
    }

    public void clearItems() {
        items.clear();
    }

    public Item[] getItems() {
        return items.toArray(new Item[0]);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.setItems(NbtHelper.readItems(nbt, "Ingredients"));
        this.hasWater = nbt.getBoolean("ContainsWater");

        this.sync();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtHelper.writeItems(nbt, "Ingredients", this.getItems());
        nbt.putBoolean("ContainsWater", this.hasWater);

        this.sync();
        return super.writeNbt(nbt);
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        this.setItems(NbtHelper.readItems(tag, "Ingredients"));
        this.hasWater = tag.getBoolean("ContainsWater");
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        NbtHelper.writeItems(tag, "Ingredients", this.getItems());
        tag.putBoolean("ContainsWater", this.hasWater);

        return tag;
    }
}
