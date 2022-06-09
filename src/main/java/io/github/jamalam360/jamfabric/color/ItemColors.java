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

package io.github.jamalam360.jamfabric.color;

import io.github.jamalam360.jamfabric.JamModInit;
import io.github.jamalam360.jamfabric.util.Utils;
import io.github.jamalam360.jamfabric.util.helper.NativeImageHelper;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jamalam360
 */
public class ItemColors {
    private static final Map<Item, Color> CACHE = new HashMap<>();

    public static Color getAverageItemColor(List<Item> items) {
        Color[] colors = new Color[items.size()];

        for (int i = 0; i < items.size(); i++) {
            colors[i] = getAverageItemColor(items.get(i));
        }

        return JamModInit.CONFIG.jamOptions.colorProviderType.getProvider().getAverageColor(colors);
    }

    public static Color getAverageItemColor(Item item) {
        if (CACHE.containsKey(item)) {
            return CACHE.get(item);
        } else {
            AverageColorProvider provider = JamModInit.CONFIG.jamOptions.colorProviderType.getProvider();
            Color color = provider.getAverageColor(NativeImageHelper.getColors(NativeImageHelper.getNativeImage(item)));
            CACHE.put(item, color);
            return color;
        }
    }
}
