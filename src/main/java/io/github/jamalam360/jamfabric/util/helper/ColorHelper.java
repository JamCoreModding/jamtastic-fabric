package io.github.jamalam360.jamfabric.util.helper;

/**
 * @author Jamalam360
 */
public class ColorHelper {
    public static int[] unpackRgbaColor(int color) {
        return new int[]{
                color & 255, // Red
                (color >> 8) & 255, // Green
                (color >> 16) & 255, // Blue
                (color >> 24) & 255 // Alpha
        };
    }
}
