package io.github.jamalam360.jamfabric.data;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author Jamalam360
 */
public record JamIngredient(Item item, int hunger, float saturation) {
    public static JamIngredient ofJson(JsonObject json) {
        Identifier itemId = new Identifier(json.get("item").getAsString());
        Item item = Registry.ITEM.get(itemId);
        int hunger = json.get("hunger").getAsInt();
        float saturation = json.get("saturation").getAsFloat();
        return new JamIngredient(item, hunger, saturation);
    }

    public static JamIngredient ofNbt(String nbt) {
        String[] parts = nbt.split(";");
        Identifier itemId = new Identifier(parts[0]);
        Item item = Registry.ITEM.get(itemId);
        int hunger = Integer.parseInt(parts[1]);
        float saturation = Float.parseFloat(parts[2]);
        return new JamIngredient(item, hunger, saturation);
    }

    public String toNbt() {
        return Registry.ITEM.getId(item) + ";" + hunger + ";" + saturation;
    }
}
