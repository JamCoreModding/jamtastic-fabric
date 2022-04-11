package io.github.jamalam360.jamfabric.data;

import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jamalam360
 */
public class JamIngredientRegistry {
    private static final Map<Item, JamIngredient> CUSTOM_INGREDIENTS = new HashMap<>();

    public static void register(JamIngredient ingredient) {
        CUSTOM_INGREDIENTS.put(ingredient.item(), ingredient);
    }

    public static boolean has(Item item) {
        return CUSTOM_INGREDIENTS.containsKey(item);
    }

    public static JamIngredient get(Item item) {
        return CUSTOM_INGREDIENTS.get(item);
    }
}
