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

package io.github.jamalam360.jamfabric.util;

import com.mojang.datafixers.util.Pair;
import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.data.JamIngredient;
import io.github.jamalam360.jamfabric.util.color.Color;
import io.github.jamalam360.jamfabric.util.color.ColorHelper;
import io.github.jamalam360.jamfabric.util.helper.NbtHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jamalam360
 */
public class Jam {
    public static final String INGREDIENTS_BASE_KEY = "Ingredients";

    private final List<JamIngredient> ingredients = new ArrayList<>();
    private final List<Pair<StatusEffectInstance, Float>> effects = new ArrayList<>();
    private final JamUpdateFunction func;
    private JamUpdateFunction clearListener = null;

    private int hunger;
    private float saturation;

    public Jam(JamIngredient... ingredients) {
        this.ingredients.addAll(Arrays.asList(ingredients));
        this.func = () -> {
        };
        this.recalculate();
    }

    public Jam(JamUpdateFunction func, JamIngredient... ingredients) {
        this.ingredients.addAll(Arrays.asList(ingredients));
        this.func = func;
        this.recalculate();
    }

    public void setClearListener(JamUpdateFunction clearListener) {
        this.clearListener = clearListener;
    }

    @SuppressWarnings("ConstantConditions")
    public void add(Item item) {
        if (item.isFood()) {
            ingredients.add(new JamIngredient(item, item.getFoodComponent().getHunger(), item.getFoodComponent().getSaturationModifier()));
            this.recalculate();
        }
    }

    public void addRaw(JamIngredient ingredient) {
        ingredients.add(ingredient);
        this.recalculate();
    }

    public Item removeLast() {
        JamIngredient ingredient = ingredients.remove(ingredients.size() - 1);
        this.recalculate();
        return ingredient.item();
    }

    public void clear() {
        ingredients.clear();
        this.recalculate();

        if (clearListener != null) {
            clearListener.update();
        }
    }

    public int ingredientsSize() {
        return ingredients.size();
    }

    public List<Item> ingredients() {
        return ingredients.stream().map(JamIngredient::item).collect(Collectors.toList());
    }

    public Color getColor() {
        return ColorHelper.getAverageItemColor(ingredients.stream().map(JamIngredient::item).collect(Collectors.toList()));
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

    public int hunger() {
        return hunger;
    }

    public List<Pair<StatusEffectInstance, Float>> effectsRaw() {
        return effects;
    }

    public List<StatusEffectInstance> effects() {
        List<StatusEffectInstance> finalEffects = new ArrayList<>();
        this.effects.forEach((pair) -> finalEffects.add(pair.getFirst()));

        return finalEffects;
    }

    public float saturation() {
        return saturation;
    }

    @SuppressWarnings("ConstantConditions")
    public void recalculate() {
        this.hunger = 0;
        this.saturation = 0;
        this.effects.clear();

        for (JamIngredient ingredient : ingredients) {
            this.hunger += ingredient.hunger();
            this.saturation += ingredient.saturation();
            if (ingredient.item().isFood()) {
                this.effects.addAll(ingredient.item().getFoodComponent().getStatusEffects());
            }
        }

        this.func.update();
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
            JamModInit.LOGGER.warn("Failed to load jam from NBT, this is likely due to an update in the NBT format, and can be ignored.");

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

    @FunctionalInterface
    public interface JamUpdateFunction {
        void update();
    }
}
