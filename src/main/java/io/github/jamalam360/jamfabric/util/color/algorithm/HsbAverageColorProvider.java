package io.github.jamalam360.jamfabric.util.color.algorithm;

import io.github.jamalam360.jamfabric.util.color.AverageColorProvider;
import io.github.jamalam360.jamfabric.util.color.Color;

/**
 * @author Jamalam360
 */
public class HsbAverageColorProvider implements AverageColorProvider {
    @Override
    public Color getAverageColor(Color[] colors) {
        float h = 0;
        float s = 0;
        float b = 0;
        float total = 0;
        for (Color color : colors) {
            if (color != null) {
                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                h += hsb[0] * hsb[2];
                s += hsb[1] * hsb[2];
                b += hsb[2];
                total += hsb[2];
            }
        }
        h /= total;
        s /= total;
        b /= total;
        return new Color(Color.HSBtoRGB(h, s, b));
    }
}
