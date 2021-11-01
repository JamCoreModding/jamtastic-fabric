package io.github.jamalam360.jamfabric.block;

import io.github.jamalam360.jamfabric.JamColorUtil;
import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.NbtHelper;
import io.github.jamalam360.jamfabric.util.Color;
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
    private boolean hasWater = false;
    private boolean hasSugar = false;
    public static final int CAPACITY = 4;
    public Color cachedColor = new Color(255, 255, 255); // To avoid recalculating the color every frame.

    public JamPotBlockEntity(BlockPos pos, BlockState state) {
        super(JamModInit.JAM_POT_BLOCK_ENTITY, pos, state);
    }

    public boolean canInsertWater() {
        return !hasWater;
    }

    public boolean canInsertSugar() {
        return !hasSugar && hasWater;
    }

    public boolean canInsertIngredients() {
        return hasSugar && hasWater && items.size() < CAPACITY;
    }

    public void setFilledWater(boolean filled) {
        this.hasWater = filled;
    }

    public void setFilledSugar(boolean filled) {
        this.hasSugar = filled;
    }

    public boolean hasWater() {
        return this.hasWater;
    }

    public boolean hasSugar() {
        return this.hasSugar;
    }

    public void addItems(Item... newItems) {
        items.addAll(List.of(newItems));
        this.updateColor();
    }

    public Item removeLastItem() {
        Item item = items.remove(items.size() - 1);
        this.updateColor();
        return item;
    }

    public void clearItems() {
        items.clear();
        this.updateColor();
    }

    public Item[] getItems() {
        return items.toArray(new Item[0]);
    }

    public void updateColor() {
        this.cachedColor = JamColorUtil.getAverageColourFromItems(this.getItems());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.clearItems();
        this.addItems(NbtHelper.readItems(nbt, "Ingredients"));
        this.hasWater = nbt.getBoolean("ContainsWater");
        this.hasSugar = nbt.getBoolean("ContainsSugar");

        this.sync();
        this.updateColor();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtHelper.writeItems(nbt, "Ingredients", this.getItems());
        nbt.putBoolean("ContainsWater", this.hasWater);
        nbt.putBoolean("ContainsSugar", this.hasSugar);

        this.sync();
        this.updateColor();

        return super.writeNbt(nbt);
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        this.clearItems();
        this.addItems(NbtHelper.readItems(tag, "Ingredients"));
        this.hasWater = tag.getBoolean("ContainsWater");
        this.hasSugar = tag.getBoolean("ContainsSugar");

        this.updateColor();
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        NbtHelper.writeItems(tag, "Ingredients", this.getItems());
        tag.putBoolean("ContainsWater", this.hasWater);
        tag.putBoolean("ContainsSugar", this.hasSugar);

        this.updateColor();

        return tag;
    }
}
