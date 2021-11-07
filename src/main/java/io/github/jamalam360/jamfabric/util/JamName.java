package io.github.jamalam360.jamfabric.util;

import io.github.jamalam360.jamfabric.JamNbtHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jamalam360
 */
public class JamName {
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

        Item[] items = JamNbtHelper.readItems(nbt, "Ingredients");

        if (items.length == 0) {
            return "";
        }

        boolean hasEffects = JamNbtHelper.getJamJarEffects(nbt).size() > 0;

        if (hasEffects) {
            int badCount = 0;
            int goodCount = 0;

            List<StatusEffectInstance> effects = new ArrayList<>();
            JamNbtHelper.getJamJarEffects(nbt).forEach(pair -> effects.add(pair.getFirst()));

            for (StatusEffectInstance instance : effects) {
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

        if (items.length == 1) {
            template = TEMPLATES[0];
        } else if (items.length == 2) {
            template = TEMPLATES[1];
        } else {
            template = random(TEMPLATES);
        }

        // Fill the template

        Item[] itemsCopy = items.clone();

        while (template.contains("%item%")) {
            int randInt = RANDOM.nextInt(itemsCopy.length);

            while (itemsCopy[randInt] == null) {
                randInt = RANDOM.nextInt(itemsCopy.length);
            }

            template = template.replaceFirst("%item%", I18n.translate(itemsCopy[randInt].getTranslationKey()));
            itemsCopy[randInt] = null;
        }

        template = template.replaceFirst("%noun%", random(JAM_NOUNS));

        sb.append(template);

        return sb.toString();
    }

    private static String random(String[] arr) {
        return arr[RANDOM.nextInt(arr.length)];
    }
}
