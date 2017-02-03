package com.metjka.vort.precessing

import java.awt.Color
import java.awt.image.ImageFilter

class SepiaFilter(val fastImage: FastImage) : Filter {

    val width = fastImage.width
    val height = fastImage.height

    override fun filter(): FastImage {
        val image = FastImage(width, height)
        for (x in 0..width - 1) {
            for (y in 0..height - 1) {
                val color = Color(fastImage.getARGB(x, y)!!)
                val r: Int = (color.red * 0.3).toInt().clamp()
                val g: Int = (color.green * 0.59).toInt().clamp()
                val b: Int = (color.blue * 0.11).toInt().clamp()

                val gray = (r + g + b).clamp()

                val r1 = (gray + 110).clamp()
                val g1 = (gray + 65).clamp()
                val b1 = (gray + 20).clamp()

                val color1 = Color(r1, g1, b1)
                image.setARGB(x, y, color1.rgb)
            }
        }

        ImageFilter()

        return image;
    }

}