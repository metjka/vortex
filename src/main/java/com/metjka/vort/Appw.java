package com.metjka.vort;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Created by Ihor Salnikov on 17.10.2016, 5:19 PM.
 * https://github.com/metjka/VORT
 */
public class Appw {

    public static void main(String[] args) {
        BufferedImage bufferedImage = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB);
        int height = bufferedImage.getHeight();

        WritableRaster raster = bufferedImage.getRaster();
    }
}
