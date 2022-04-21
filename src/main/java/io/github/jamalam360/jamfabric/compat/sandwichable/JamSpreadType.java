package io.github.jamalam360.jamfabric.compat.sandwichable;

import io.github.foundationgames.sandwichable.items.spread.SpreadType;
import io.github.jamalam360.jamfabric.util.Jam;
import io.github.jamalam360.jamfabric.util.registry.ItemRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jamalam360
 */
public class JamSpreadType extends SpreadType {
    public static Map<ItemStack, Integer> cache = new HashMap<>();

    public JamSpreadType() {
        super(0, 0.0f, 0, ItemRegistry.JAM_JAR, ItemRegistry.JAM_JAR);
    }

    @Override
    public int getColor(ItemStack stack) {
        Jam jam = Jam.fromNbt(stack.getSubNbt("Jam"));
        if (jam.ingredientsSize() == 0) {
            return 0;
        }

        if (!cache.containsKey(stack)) {
            cache.put(stack, jam.getColor().getRGB());
        }

        return cache.get(stack);
    }

    @Override
    public int getHunger() {
        return 0;
    }

    @Override
    public float getSaturationModifier() {
        return 0;
    }

    @Override
    public List<StatusEffectInstance> getStatusEffects(ItemStack stack) {
        return Jam.fromNbt(stack.getSubNbt("Jam")).effects();
    }

    @Override
    public ItemConvertible getContainingItem() {
        return ItemRegistry.JAM_JAR;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(ItemRegistry.JAM_JAR);
    }

    @Override
    public void onPour(ItemStack container, ItemStack spread) {
        spread.setSubNbt("Jam", container.getSubNbt("Jam"));
    }

    @Override
    public String getTranslationKey(String id, ItemStack stack) {
        return "text.jamfabric.jam_spread";
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}