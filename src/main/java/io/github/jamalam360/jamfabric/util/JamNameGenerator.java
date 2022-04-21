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

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jamalam360
 */
public class JamNameGenerator {
    private static final Random RANDOM = new Random();
    public static final String BENEFICIAL_EFFECT_ADJECTIVES_KEY = "jam_names.jamfabric.beneficial_effect_adjectives";
    public static final String NON_BENEFICIAL_EFFECT_ADJECTIVES_KEY = "jam_names.jamfabric.non_beneficial_effect_adjectives";
    public static final String NOUNS_KEY = "jam_names.jamfabric.nouns";

    public static String[] BENEFICIAL_EFFECT_ADJECTIVES = new String[0];
    public static String[] NON_BENEFICIAL_EFFECT_ADJECTIVES = new String[0];
    public static String[] NOUNS = new String[0];

    private static final String[][] TEMPLATES = new String[][]{
            new String[]{"item", "noun"},
            new String[]{"item", "and", "item", "noun"},
            new String[]{"item", "item", "and", "item", "noun"}
    };

    public static String[] create(NbtCompound nbt, PlayerEntity player) {
        if (player.world.isClient) return new String[0];

        StringBuilder sb = new StringBuilder();
        Jam jam = Jam.fromNbt(nbt);

        if (jam.ingredientsSize() == 0) {
            return new String[0];
        }

        boolean hasEffects = jam.effectsRaw().size() > 0;

        if (hasEffects) {
            int badCount = 0;
            int goodCount = 0;

            for (StatusEffectInstance instance : jam.effects()) {
                if (instance.getEffectType().isBeneficial()) {
                    goodCount++;
                } else {
                    badCount++;
                }
            }

            if (badCount > goodCount) {
                sb.append(random(NON_BENEFICIAL_EFFECT_ADJECTIVES));
            } else if (badCount < goodCount) {
                sb.append(random(BENEFICIAL_EFFECT_ADJECTIVES));
            } else {
                sb.append(RANDOM.nextBoolean() ? random(BENEFICIAL_EFFECT_ADJECTIVES) : random(NON_BENEFICIAL_EFFECT_ADJECTIVES));
            }

            sb.append(" ");
        } else {
            for (Item item : jam.ingredients()) {
                if (item.getTranslationKey().contains("raw")) {
                    if (RANDOM.nextBoolean()) {
                        sb.append(random(NON_BENEFICIAL_EFFECT_ADJECTIVES));
                        sb.append(" ");
                        break;
                    }
                }
            }
        }

        String[] template;
        List<Item> uniqueItems = jam.ingredients().stream().distinct().collect(Collectors.toList());

        if (uniqueItems.size() == 1) {
            template = TEMPLATES[0];
        } else if (uniqueItems.size() == 2) {
            template = TEMPLATES[1];
        } else {
            template = random(TEMPLATES);
        }

        for (String s : template) {
            if (s.equals("item")) {
                int randInt = RANDOM.nextInt(uniqueItems.size());
                sb.append(uniqueItems.remove(randInt).getTranslationKey());
            } else if (s.equals("noun")) {
                sb.append(random(NOUNS));
            } else {
                sb.append(s);
            }

            sb.append(" ");
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString().split(" ");
    }

    private static <T> T random(T[] arr) {
        return arr[RANDOM.nextInt(arr.length)];
    }
}
