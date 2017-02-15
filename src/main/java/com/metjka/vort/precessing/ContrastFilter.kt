package com.metjka.vort.precessing

import java.awt.Color

class ContrastFilter(val fastImage: FastImage, val contrastVal: Int) : Filter {

    val width = fastImage.width
    val height = fastImage.height

    override fun filter(): FastImage {

        val contrast = Math.pow((100 + contrastVal).toDouble()/100, 2.0)

        val image = FastImage(width, height)
        for (x in 0..width - 1) {
            for (y in 0..height - 1) {
                val color = Color(fastImage.getARGB(x, y)!!)
                var R = color.red
                var G = color.green
                var B = color.blue

                R = ((((R / 255.0) - 0.5) * contrast + 0.5) * 255.0).toInt().clamp()
                G = ((((G / 255.0) - 0.5) * contrast + 0.5) * 255.0).toInt().clamp()
                B = ((((B / 255.0) - 0.5) * contrast + 0.5) * 255.0).toInt().clamp()

                image.setARGB(x, y, Color(R, G, B).rgb)
            }
        }
        return image;
    }
}
