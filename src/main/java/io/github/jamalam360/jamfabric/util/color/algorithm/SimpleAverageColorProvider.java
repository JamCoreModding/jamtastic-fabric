package io.github.jamalam360.jamfabric.util.color.algorithm;

import io.github.jamalam360.jamfabric.util.color.AverageColorProvider;
import io.github.jamalam360.jamfabric.util.color.Color;

/**
 * @author Jamalam360
 */
public class SimpleAverageColorProvider implements AverageColorProvider {
    @Override
    public Color getAverageColor(Color[] colors) {
        float r = 0;
        float g = 0;
        float b = 0;
        int total = 0;

        for (Color color : colors) {
            if (color != null) {
                r += color.getRed();
                g += color.getGreen();
                b += color.getBlue();
                total++;
            }
        }

        long avgR = Math.round(r / total);
        long avgG = Math.round(g / total);
        long avgB = Math.round(b / total);

        return new Color((int) avgR, (int) avgG, (int) avgB);
    }
}
