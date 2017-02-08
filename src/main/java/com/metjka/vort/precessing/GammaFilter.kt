package com.metjka.vort.precessing

import java.awt.Color

class GammaFilter(val fastImage: FastImage, val gamma: Double) : Filter {

    val width = fastImage.width
    val height = fastImage.height

    override fun filter(): FastImage {

        val gammaP: Double = 1 / (5 - gamma)

        val image = FastImage(width, height)
        for (x in 0..width - 1) {
            for (y in 0..height - 1) {
                val color = Color(fastImage.getARGB(x, y)!!)
                var R = color.red
                var G = color.green
                var B = color.blue

                R = (255 * Math.pow(R.toDouble() / 255.0, gammaP)).toInt().clamp()
                G = (255 * Math.pow(G.toDouble() / 255.0, gammaP)).toInt().clamp()
                B = (255 * Math.pow(B.toDouble() / 255.0, gammaP)).toInt().clamp()

                val red = (255 * Math.pow(R.toDouble() / 255.toDouble(), gammaP)).toInt()
                val green = (255 * Math.pow(G.toDouble() / 255.toDouble(), gammaP)).toInt()
                val blue = (255 * Math.pow(B.toDouble() / 255.toDouble(), gammaP)).toInt()

                image.setARGB(x, y, Color(red, green, blue).rgb)
            }
        }
        return image;
    }


}