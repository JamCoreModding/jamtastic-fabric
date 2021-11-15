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

import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jamalam360
 */
public class JamNameGenerator {
    private static final Random RANDOM = new Random();

    //region Templates
    private static final String[] TEMPLATES = new String[]{
            "%item% %noun%",
            "%item% and %item% %noun%",
            "%item%, %item% and %item% %noun%"
    };

    private static final String[] BENEFICIAL_EFFECT_ADJECTIVES = new String[]{
            "Premium",
            "Luxury"
    };

    private static final String[] NON_BENEFICIAL_EFFECT_ADJECTIVES = new String[]{
            "Disgusting",
            "Revolting",
            "Sickening",
            "Unpleasant",
            "Nauseating"
    };

    private static final String[] JAM_NOUNS = new String[]{
            "Jam",
            "Conserve",
            "Preserve"
    };
    //endregion

    public static String create(NbtCompound nbt) {
        StringBuilder sb = new StringBuilder();

        Jam jam = Jam.fromNbt(nbt);

        if (jam.ingredientsSize() == 0) {
            return "";
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
        }

        String template;
        List<Item> uniqueItems = jam.ingredients().stream().distinct().collect(Collectors.toList());

        if (uniqueItems.size() == 1) {
            template = TEMPLATES[0];
        } else if (uniqueItems.size() == 2) {
            template = TEMPLATES[1];
        } else {
            template = random(TEMPLATES);
        }

        // Fill the template

        while (template.contains("%item%")) {
            int randInt = RANDOM.nextInt(uniqueItems.size());
            template = template.replaceFirst("%item%", I18n.translate(uniqueItems.remove(randInt).getTranslationKey()));
        }

        template = template.replaceFirst("%noun%", random(JAM_NOUNS));

        sb.append(template);

        return sb.toString();
    }

    private static String random(String[] arr) {
        return arr[RANDOM.nextInt(arr.length)];
    }
}
