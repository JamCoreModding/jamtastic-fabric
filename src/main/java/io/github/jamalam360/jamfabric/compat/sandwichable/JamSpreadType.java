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

package io.github.jamalam360.jamfabric.compat.sandwichable;

import io.github.foundationgames.sandwichable.items.spread.SpreadType;
import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.JamNbtHelper;
import io.github.jamalam360.jamfabric.util.JamColor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jamalam360
 */
public class JamSpreadType extends SpreadType {
    public static Map<ItemStack, Integer> cache = new HashMap<>();

    public JamSpreadType() {
        super(0, 0.0f, 0, JamModInit.JAM_JAR, JamModInit.JAM_JAR);
    }

    @Override
    public int getColor(ItemStack stack) {
        if (JamNbtHelper.readItems(stack.getSubNbt("JamNbt"), "Ingredients").length == 0) {
            return 0;
        }

        if (!cache.containsKey(stack)) {
            cache.put(stack, JamColor.getAverageItemColor(JamNbtHelper.readItems(stack.getSubNbt("JamNbt"), "Ingredients")).getRGB());
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
        List<StatusEffectInstance> effects = new ArrayList<>();
        JamNbtHelper.getJamJarEffects(stack.getOrCreateNbt()).forEach(pair -> effects.add(pair.getFirst()));
        return effects;
    }

    @Override
    public ItemConvertible getContainingItem() {
        return JamModInit.JAM_JAR;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(JamModInit.JAM_JAR);
    }

    @Override
    public void onPour(ItemStack container, ItemStack spread) {
        spread.setSubNbt("JamNbt", container.getOrCreateNbt());
    }

    @Override
    public String getTranslationKey(String id, ItemStack stack) {
        return "item.jamfabric.jam_jar";
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}
