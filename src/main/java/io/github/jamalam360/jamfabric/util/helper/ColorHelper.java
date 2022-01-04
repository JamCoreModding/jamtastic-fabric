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

package io.github.jamalam360.jamfabric.util.helper;

import io.github.jamalam360.jamfabric.util.Color;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.item.Item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jamalam360
 */
public class ColorHelper {
    /*
     *  Responsible for taking in an array of items and returning an average colour, this involves:
     *      - Getting a NativeImage off of an Item
     *      - Looping over the pixels in the NativeImage and get their colour
     *      - If the pixel is white or black discard it
     *      - Get the average value of all those pixels
     *      - Repeat all of this for each item in the array and then average those colours
     *      - Return the average colour
     *  This class should also maintain a cache of items and their colours, so that the process is not repeated unnecessarily
     *
     * To average colours, we square the average of the square root of every pixel's colour (suggested by FoundationGames)
     */

    private static final Map<Item, Color> CACHE = new HashMap<>();

    //region Item Color Retrieval

    /**
     * Calculate the average color of all the items in the var args
     */
    public static Color getAverageItemColor(List<Item> items) {
        Color[] colors = new Color[items.size()];

        for (int i = 0; i < items.size(); i++) {
            colors[i] = getAverageItemColor(items.get(i));
        }

        return getAverageColor(colors);
    }

    /**
     * Calculate the average colour of an item
     */
    public static Color getAverageItemColor(Item item) {
        if (CACHE.containsKey(item)) {
            return CACHE.get(item);
        } else {
            Color color = getAverageColor(getColors(NativeImageHelper.getNativeImage(item)));
            CACHE.put(item, color);
            return color;
        }
    }
    //endregion

    //region Color Manipulation

    /**
     * Gets an array of all the colors of each pixel in a NativeImage
     */
    private static Color[] getColors(NativeImage image) {
        Color[] colors = new Color[image.getWidth() * image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int[] pixelColors = ColorHelper.unpackRgbaColor(image.getColor(x, y));
                if ((pixelColors[0] != 255 && pixelColors[1] != 255 && pixelColors[2] != 255) && (pixelColors[0] != 0 && pixelColors[1] != 0 && pixelColors[2] != 0)) {
                    colors[x + y] = new Color(pixelColors[0], pixelColors[1], pixelColors[2]);
                }
            }
        }

        return colors;
    }

    /**
     * Calculates the average colour of an array of colors
     */
    private static Color getAverageColor(Color[] colors) {
        float r = 0;
        float g = 0;
        float b = 0;
        int total = 0;
        for (Color color : colors) {
            if (color != null) {
                r += color.getRed() * color.getRed();
                g += color.getGreen() * color.getGreen();
                b += color.getBlue() * color.getBlue();
                total++;
            }
        }

        long avgR = Math.round(Math.sqrt(r / total));
        long avgG = Math.round(Math.sqrt(g / total));
        long avgB = Math.round(Math.sqrt(b / total));

        return new Color((int) avgR, (int) avgG, (int) avgB);
    }

    private static int[] unpackRgbaColor(int color) {
        return new int[]{
                color & 255, // Red
                (color >> 8) & 255, // Green
                (color >> 16) & 255, // Blue
                (color >> 24) & 255 // Alpha
        };
    }
    //endregion
}
