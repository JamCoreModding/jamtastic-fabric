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
