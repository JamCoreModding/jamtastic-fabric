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

package io.github.jamalam360.jamfabric.jam;

import com.mojang.datafixers.util.Pair;
import io.github.jamalam360.jamfabric.color.Color;
import io.github.jamalam360.jamfabric.color.ItemColors;
import io.github.jamalam360.jamfabric.data.JamIngredient;
import io.github.jamalam360.jamfabric.util.helper.NbtHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jamalam360
 */
public class Jam {
    public static final String INGREDIENTS_BASE_KEY = "Ingredients";
    private static final Logger LOGGER = LogManager.getLogger("Jamtastic/Jam");

    private final List<JamIngredient> ingredients = new ArrayList<>();
    private final List<Pair<StatusEffectInstance, Float>> effects = new ArrayList<>();
    private final List<JamStateListener> listeners = new ArrayList<>();

    private int hunger;
    private float saturation;

    public Jam(JamIngredient... ingredients) {
        this.ingredients.addAll(Arrays.asList(ingredients));
        this.recalculate();
    }
    
    public void addListener(JamStateListener listener) {
        listeners.add(listener);
    }
    
    public void add(Item item) {
        if (item.isFood()) {
            assert item.getFoodComponent() != null;
            
            ingredients.add(new JamIngredient(item, item.getFoodComponent().getHunger(), item.getFoodComponent().getSaturationModifier()));
            this.recalculate();
        } else {
            LOGGER.warn("Item {} is not a food item. This is a bug, and should be reported.", item.getName());
        }
    }

    public void addRaw(JamIngredient ingredient) {
        ingredients.add(ingredient);
        this.recalculate();
    }

    public Item removeLast() {
        JamIngredient ingredient = ingredients.remove(this.getIngredients().size() - 1);
        this.recalculate();
        return ingredient.item();
    }

    public void clear() {
        ingredients.clear();
        this.recalculate();

        for (JamStateListener listener : listeners) {
            listener.onCleared();
        }
    }

    public List<JamIngredient> getIngredients() {
        return ingredients;
    }

    public List<Item> getItemIngredients() {
        return ingredients.stream().map(JamIngredient::item).collect(Collectors.toList());
    }

    public Color getColor() {
        return ItemColors.getAverageItemColor(ingredients.stream().map(JamIngredient::item).collect(Collectors.toList()));
    }

    public List<Text> getTooltipContent() {
        Map<Item, Integer> tooltipMap = new HashMap<>();
        List<Text> finalTooltip = new ArrayList<>();

        for (JamIngredient ingredient : this.ingredients) {
            if (tooltipMap.containsKey(ingredient.item())) {
                tooltipMap.put(ingredient.item(), tooltipMap.get(ingredient.item()) + 1);
            } else {
                tooltipMap.put(ingredient.item(), 1);
            }
        }

        if (tooltipMap.isEmpty()) {
            finalTooltip.add(new TranslatableText("tooltip.jamfabric.empty_tooltip").styled(s -> s.withColor(Formatting.AQUA)));
        } else {
            finalTooltip.add(new TranslatableText("tooltip.jamfabric.hunger_tooltip", this.hunger).styled(s -> s.withColor(Formatting.AQUA)));
            finalTooltip.add(new LiteralText(""));
            tooltipMap.forEach((key, value) -> finalTooltip.add(new TranslatableText(key.getTranslationKey()).append(" x" + value).styled((style -> style.withColor(Formatting.GOLD)))));
        }

        return finalTooltip;
    }

    public float getSaturation() {
        return saturation;
    }

    public int getHunger() {
        return hunger;
    }

    public List<Pair<StatusEffectInstance, Float>> getRawEffects() {
        return effects;
    }

    public List<StatusEffectInstance> getEffects() {
        List<StatusEffectInstance> finalEffects = new ArrayList<>();
        this.effects.forEach((pair) -> finalEffects.add(pair.getFirst()));

        return finalEffects;
    }

    public void recalculate() {
        this.hunger = 0;
        this.saturation = 0;
        this.effects.clear();

        for (JamIngredient ingredient : ingredients) {
            this.hunger += ingredient.hunger();
            this.saturation += ingredient.saturation();
            if (ingredient.item().isFood()) {
                assert ingredient.item().getFoodComponent() != null;
                this.effects.addAll(ingredient.item().getFoodComponent().getStatusEffects());
            }
        }

        for (JamStateListener listener : listeners) {
            listener.onUpdated();
        }
    }

    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        NbtHelper.writeJamIngredients(compound, INGREDIENTS_BASE_KEY, ingredients.toArray(new JamIngredient[0]));
        return compound;
    }

    public static Jam fromNbt(@Nullable NbtCompound compound) {
        try {
            if (compound != null) {
                return new Jam(NbtHelper.readJamIngredients(compound, INGREDIENTS_BASE_KEY));
            } else {
                return new Jam();
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to load jam from NBT, this is likely due to an update in the NBT format, and can be ignored.");

            if (compound == null) {
                LOGGER.warn("Failed to recover from NBT error: NBT compound was null.");
                return new Jam();
            }

            // This parses the _old_ NBT format - don't touch!

            Item[] ingredients = NbtHelper.readItems(compound, INGREDIENTS_BASE_KEY);

            for (int i = 0; i < compound.getInt(INGREDIENTS_BASE_KEY + "Length"); i++) {
                compound.remove(INGREDIENTS_BASE_KEY + i);
            }
            compound.remove(INGREDIENTS_BASE_KEY + "Length");

            Jam jam = new Jam();

            for (Item ingredient : ingredients) {
                jam.add(ingredient);
            }

            NbtHelper.writeJamIngredients(compound, INGREDIENTS_BASE_KEY, jam.ingredients.toArray(new JamIngredient[0]));

            return jam;
        }
    }
}
