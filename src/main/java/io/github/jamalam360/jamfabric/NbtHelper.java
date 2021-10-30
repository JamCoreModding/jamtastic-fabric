package io.github.jamalam360.jamfabric;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stolen from LibJam
 * @author Jamalam360
 */
public class NbtHelper {
    public static NbtCompound writeItems(NbtCompound compound, String baseKey, Item... items) {
        ArrayList<Item> appendedItems = new ArrayList<>(List.of(items));
        appendedItems.addAll(Arrays.asList(readItems(compound, baseKey))); // Adds previously saved items

        compound.putInt(baseKey + "Length", appendedItems.size());
        for (int i = 0; i < appendedItems.size(); i++) {
            compound.putString(baseKey + i, Registry.ITEM.getId(appendedItems.get(i)).toString());
        }

        return compound;
    }

    public static Item[] readItems(NbtCompound compound, String baseKey) {
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < compound.getInt(baseKey + "Length"); i++) {
            items.add(Registry.ITEM.get(new Identifier(compound.getString(baseKey + i))));
        }

        return items.toArray(new Item[0]);
    }

    public static NbtCompound writeItem(NbtCompound compound, String key, Item item) {
        compound.putString(key, Registry.ITEM.getId(item).toString());
        return compound;
    }

    public static Item readItem(NbtCompound compound, String key) {
        return Registry.ITEM.get(new Identifier(compound.getString(key)));
    }

    public static NbtCompound writeItemStack(NbtCompound compound, String key, ItemStack itemStack) {
        NbtCompound subTag = new NbtCompound();
        writeItem(subTag, "Item", itemStack.getItem());
        subTag.putInt("Count", itemStack.getCount());
        subTag.put("Nbt", itemStack.getOrCreateNbt());

        compound.put(key, subTag);
        return compound;
    }

    public static ItemStack readItemStack(NbtCompound compound, String key) {
        NbtCompound subTag = compound.getCompound(key);
        Item item = readItem(subTag, "Item");
        int count = subTag.getInt("Count");
        NbtCompound nbt = subTag.getCompound("Nbt");

        ItemStack result = new ItemStack(item, count);
        result.setNbt(nbt);

        return result;
    }
}
