package io.github.jamalam360.jamfabric.util;

import io.github.jamalam360.jamfabric.JamModInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jamalam360
 */
public class JamColor {
    /*
     *  Responsible for taking in an array of items and returning an average colour, this involves:
     *      - Getting a NativeImage off of an Item
     *      - Looping over the pixels in the NativeImage and get their colour
     *      - If the pixel is white or black discard it
     *      - Get the average value of all those pixels
     *      - Repeat all of this for each item in the array and then average those colours
     *      - Return the average colour
     *  This class should also maintain a cache of items and their colours, so that the process is not repeated unnecessarily
     */

    private static final Map<Item, Color> CACHE = new HashMap<>();
    private static final Map<String, String> TEXTURE_OVERRIDES = new HashMap<>();

    //region Item Color Retrieval
    /**
     * Calculate the average color of all the items in the var args
     */
    public static Color getAverageItemColor(Item... items) {
        Color[] colors = new Color[items.length];

        for (int i = 0; i < items.length; i++) {
            colors[i] = getAverageItemColor(items[i]);
            System.out.println(colors[i]);
        }

        return getAverageColor(colors);
    }

    /**
     * Calculate the average colour of an item
     */
    public static Color getAverageItemColor(Item item) {
        /*if (CACHE.containsKey(item)) {
            return CACHE.get(item);
        } else {*/
            Color color = getAverageColor(getColors(getNativeImage(item)));
            CACHE.put(item, color);
            return color;
        //}
    }
    //endregion

    /**
     * Returns a NativeImage of an Items texture, taking into account the overrides set in TEXTURE_OVERRIDES
     */
    private static @Nullable NativeImage getNativeImage(Item item) {
        try {
            Identifier id = Registry.ITEM.getId(item);
            String namespace = id.getNamespace();
            String path = id.getPath();

            if (TEXTURE_OVERRIDES.containsKey(path)) {
                path = TEXTURE_OVERRIDES.get(path);
            }

            Resource texture = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier(namespace, "textures/item/" + path + ".png"));
            return NativeImage.read(texture.getInputStream());
        } catch (IOException e) {
            JamModInit.LOGGER.log(Level.ERROR, "Caught an error while retrieving native image");
            e.printStackTrace();
        }

        return null;
    }

    //region Color Manipulation
    /**
     * Gets an array of all the colors of each pixel in a NativeImage
     */
    private static Color[] getColors(NativeImage image) {
        Color[] colors = new Color[image.getWidth() * image.getHeight()];
        System.out.println(image.getWidth());
        System.out.println(image.getHeight());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                System.out.println(x + " " + y);
                int[] pixelColors = JamColor.unpackRgbaColor(image.getPixelColor(x, y));
                System.out.println(Arrays.toString(pixelColors));
                if ((pixelColors[0] != 255 && pixelColors[1] != 255 && pixelColors[2] != 255) && (pixelColors[0] != 0 && pixelColors[1] != 0 && pixelColors[2] != 0)) {
                    colors[x + y] = new Color(pixelColors[0], pixelColors[1], pixelColors[2]);
                    System.out.println("Added color");
                }
                System.out.println();
            }
        }

        return colors;
    }

    /**
     * Calculates the average colour of an array of colors
     */
    private static Color getAverageColor(Color[] colors) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int total = 0;

        for (Color color : colors) {
            if (color != null) {
                red += color.getRed();
                green += color.getGreen();
                blue += color.getBlue();
                total++;
            }
        }

        return new Color(red / total, green / total, blue / total);
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

    //region Static Init
    static {
        TEXTURE_OVERRIDES.put("enchanted_golden_apple", "golden_apple");
    }
    //endregion
}
