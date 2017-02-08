package com.metjka.vort.precessing

import java.awt.Color

class BrightnessFilter(val fastImage: FastImage, val value:Int) : Filter {

    val width = fastImage.width
    val height = fastImage.height

    override fun filter(): FastImage {
        val image = FastImage(width, height)
        for (x in 0..width - 1) {
            for (y in 0..height - 1) {
                val color = Color(fastImage.getARGB(x, y)!!)
                var R = color.red
                var G = color.green
                var B = color.blue

                R = (R+ value).clamp()
                G = (G+ value).clamp()
                B = (B+ value).clamp()

                image.setARGB(x, y, Color(R,G,B).rgb)
            }
        }
        return image;
    }


}