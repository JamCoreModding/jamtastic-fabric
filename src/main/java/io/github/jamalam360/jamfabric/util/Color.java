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

package io.github.jamalam360.jamfabric.util;

/**
 * '''Liberated''' from AWT because that package breaks on macOS.
 */
@SuppressWarnings({"SameParameterValue", "PointlessBitwiseExpression"})
public class Color {
    int value;

    private static void testColorValueRange(int r, int g, int b, int a) {
        boolean rangeError = false;
        String badComponentString = "";

        if (a < 0 || a > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Alpha (" + a + ")";
        }
        if (r < 0 || r > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Red (" + r + ")";
        }
        if (g < 0 || g > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Green (" + g + ")";
        }
        if (b < 0 || b > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Blue (" + b + ")";
        }
        if (rangeError) {
            throw new IllegalArgumentException("Color parameter outside of expected range:"
                    + badComponentString);
        }
    }

    public Color(int rgb) {
        value = 0xff000000 | rgb;
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF) << 0);
        testColorValueRange(r, g, b, a);
    }

    public int getRed() {
        return (getRGB() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (getRGB() >> 8) & 0xFF;
    }

    public int getBlue() {
        return (getRGB() >> 0) & 0xFF;
    }

    public int getRGB() {
        return value;
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0 -> {
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                }
                case 1 -> {
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                }
                case 2 -> {
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                }
                case 3 -> {
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                }
                case 4 -> {
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                }
                case 5 -> {
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                }
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
    }

    public static float[] RGBtoHSB(int r, int g, int b, float[] hsbvals) {
        float hue, saturation, brightness;
        if (hsbvals == null) {
            hsbvals = new float[3];
        }
        int cmax = Math.max(r, g);
        if (b > cmax) cmax = b;
        int cmin = Math.min(r, g);
        if (b < cmin) cmin = b;

        brightness = ((float) cmax) / 255.0f;
        if (cmax != 0)
            saturation = ((float) (cmax - cmin)) / ((float) cmax);
        else
            saturation = 0;
        if (saturation == 0)
            hue = 0;
        else {
            float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
            float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
            float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
            if (r == cmax)
                hue = bluec - greenc;
            else if (g == cmax)
                hue = 2.0f + redc - bluec;
            else
                hue = 4.0f + greenc - redc;
            hue = hue / 6.0f;
            if (hue < 0)
                hue = hue + 1.0f;
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public String toString() {
        return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
    }
}
