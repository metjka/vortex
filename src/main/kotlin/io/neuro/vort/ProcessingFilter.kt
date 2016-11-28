package io.neuro.vort

import java.awt.Color

abstract class ProcessingFilter(fastABGRImage: FastABGRImage) {

    val width = fastABGRImage.width
    val height = fastABGRImage.height

    val pixels = DoubleArray(width * height)

    fun setPixel(x: Int, y: Int, rgb: Double) {
        pixels[x + y * width] = rgb
    }

    fun getPixel(x: Int, y: Int): Double {
        return pixels[x + y * width]
    }

    fun addPixel(x: Int, y: Int, rgb: Double) {
        val pixel: Double = getPixel(x, y)
        setPixel(x, y, pixel + rgb)
    }

    fun gray(): IntArray {
        val p: IntArray = IntArray(512 * 512)
        for (i: Int in 0..pixels.size-1) {
            var f: Double = pixels[i]
            if (f < 0.0)
                f = 0.0
            if (f > 255)
                f = 255.0

            val rgb = Color(f.toInt(), f.toInt(), f.toInt(), 255).rgb
            p[i] = rgb
        }
        return p
    }

}
