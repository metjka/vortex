package io.metjka.vortex.precessing

import java.awt.Color

class InvertFilter(val fastImage: FastImage) : Filter {

    val width = fastImage.width
    val height = fastImage.height

    override fun filter(): FastImage {
        val image = FastImage(width, height)
        for (x in 0..width - 1) {
            for (y in 0..height - 1) {
                val color = Color(fastImage.getARGB(x, y)!!)
                val r = 255 - color.red
                val g = 255 - color.green
                val b = 255 - color.blue
                val color1 = Color(r, g, b)
                image.setARGB(x, y, color1.rgb)
            }
        }
        return image;
    }


}