package com.metjka.vort.precessing;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created by isalnikov on 2/3/2017.
 */
public class Sob implements Filter {

    int w, h;
    FastImage fastImage;
    FastImage fastImage1;
    int pixel_x;
    int pixel_y;

    int sobel_x[][] = {{-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}};
    int sobel_y[][] = {{-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}};

    public Sob(FastImage fastImage) {
        w = fastImage.getWidth();
        h = fastImage.getHeight();
        this.fastImage = fastImage;
        fastImage1 = new FastImage(w, h);

    }


    @NotNull
    @Override
    public FastImage filter() {
        for (int x = 1; x < w - 2; x++) {
            for (int y = 1; y < h - 2; y++) {
                pixel_x = (sobel_x[0][0] * fastImage.getARGB(x - 1, y - 1)) + (sobel_x[0][1] * fastImage.getARGB(x, y - 1)) + (sobel_x[0][2] * fastImage.getARGB(x + 1, y - 1)) +
                        (sobel_x[1][0] * fastImage.getARGB(x - 1, y)) + (sobel_x[1][1] * fastImage.getARGB(x, y)) + (sobel_x[1][2] * fastImage.getARGB(x + 1, y)) +
                        (sobel_x[2][0] * fastImage.getARGB(x - 1, y + 1)) + (sobel_x[2][1] * fastImage.getARGB(x, y + 1)) + (sobel_x[2][2] * fastImage.getARGB(x + 1, y + 1));
                pixel_y = (sobel_y[0][0] * fastImage.getARGB(x - 1, y - 1)) + (sobel_y[0][1] * fastImage.getARGB(x, y - 1)) + (sobel_y[0][2] * fastImage.getARGB(x + 1, y - 1)) +
                        (sobel_y[1][0] * fastImage.getARGB(x - 1, y)) + (sobel_y[1][1] * fastImage.getARGB(x, y)) + (sobel_y[1][2] * fastImage.getARGB(x + 1, y)) +
                        (sobel_y[2][0] * fastImage.getARGB(x - 1, y + 1)) + (sobel_y[2][1] * fastImage.getARGB(x, y + 1)) + (sobel_x[2][2] * fastImage1.getARGB(x + 1, y + 1));

                int val = (int) Math.sqrt((pixel_x * pixel_x) + (pixel_y * pixel_y));

                if (val < 0) {
                    val = 0;
                }

                if (val > 255) {
                    val = 255;
                }

                fastImage1.setARGB(x, y, new Color(val).getRGB());
            }
        }
        return fastImage1;
    }
}
