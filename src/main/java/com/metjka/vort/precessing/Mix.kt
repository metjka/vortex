package com.metjka.vort.precessing

import java.awt.Color

class Mix(val fastImage: FastImage, val color: Color, val ratio: Float) : Filter {

    val width = fastImage.width
    val height = fastImage.height

    //TODO

    override fun filter(): FastImage {
        val image = FastImage(width, height)
        for (x in 0..width - 1) {
            for (y in 0..height - 1) {
                val colorFromPixel = Color(fastImage.getARGB(x, y)!!)
                val r:Int = (colorFromPixel.red + (color.red - colorFromPixel.red) * ratio).toInt()
                val g:Int = (colorFromPixel.red + (color.red - colorFromPixel.red) * ratio).toInt()
                val b:Int = (colorFromPixel.red + (color.red - colorFromPixel.red) * ratio).toInt()
                val color1 = Color(r, g, b)
                image.setARGB(x, y, color1.rgb)
            }
        }
        return image;
    }
}