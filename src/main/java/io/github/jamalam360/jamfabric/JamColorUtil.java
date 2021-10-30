package io.github.jamalam360.jamfabric;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.awt.*;
import java.io.IOException;

/**
 * @author Jamalam360
 */

public class JamColorUtil {
    public static Color averageColours(Color... colours) {
        float sumR = 0;
        float sumG = 0;
        float sumB = 0;

        for (Color color : colours) {
            sumR += (color.getRed() / 255f);
            sumG += (color.getGreen() / 255f);
            sumB += (color.getBlue() / 255f);
        }

        return new Color(sumR / colours.length, sumG / colours.length, sumB / colours.length);
    }

    public static Color getAverageColour(Item item) {
        try {
            ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
            Resource texture = manager.getResource(new Identifier("textures/item/" + Registry.ITEM.getId(item).getPath() + ".png"));

            NativeImage nativeImage = NativeImage.read(texture.getInputStream());

            return getAverageColourFromImage(nativeImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Color.BLACK;
    }

    private static Color getAverageColourFromImage(NativeImage img) {
        float sumR = 0;
        float sumG = 0;
        float sumB = 0;
        int totalNonEmptyPixels = 0;

        for (int x = 1; x <= 15; x++) {
            for (int y = 1; y <= 15; y++) {
                int[] components = unpackARGBColor(img.getPixelColor(x, y));
                sumR += (components[3] / 255f);
                sumG += (components[1] / 255f);
                sumB += (components[0] / 255f);

                //Checking whether there is actually any colour in this pixel, and if there is, adding it to the total we divide by at the end
                if (components[0] != 0 && components[1] != 0 && components[2] != 0 && components[3] != 0) {
                    totalNonEmptyPixels++;
                }
            }
        }

        return new Color(sumR / totalNonEmptyPixels, sumG / totalNonEmptyPixels, sumB / totalNonEmptyPixels);
    }

    /**
     * @author LambdAurora [:) thanks]
     */
    private static int[] unpackARGBColor(int color) {
        return new int[]{
                (color >> 16) & 255,
                (color >> 8) & 255,
                color & 255,
                (color >> 24) & 255
        };
    }
}
