package io.neuro.vort;

import sun.awt.image.PixelConverter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RGBImageFilter;

/**
 * Created by Ihor Salnikov on 17.10.2016, 7:20 PM.
 * https://github.com/metjka/VORT
 */
public class FastRGB extends RGBImageFilter {

    private int width;
    private int height;
    private boolean hasAlphaChannel;
    private int pixelLength;
    private byte[] pixels;

    FastRGB(BufferedImage image) {

        pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        width = image.getWidth();
        height = image.getHeight();
        hasAlphaChannel = image.getAlphaRaster() != null;
        pixelLength = 3;
        if (hasAlphaChannel) {
            pixelLength = 4;
        }

    }

    int getRGB(int x, int y) {
        int pos = (y * pixelLength * width) + (x * pixelLength);

        int argb = -16777216; // 255 alpha
        if (hasAlphaChannel) {
            argb = (((int) pixels[pos++] & 0xff) << 24); // alpha
        }

        argb += (((int) pixels[pos++] & 0xff) << 8); // green
        argb += (((int) pixels[pos++] & 0xff) << 16); // red
        argb += ((int) pixels[pos++] & 0xff); // blue
        return argb;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        return 0;
    }
}