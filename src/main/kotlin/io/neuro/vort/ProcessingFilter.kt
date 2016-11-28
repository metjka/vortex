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
        val argbArray: IntArray = IntArray(width * height)
        for (i: Int in 0..pixels.size - 1) {
            var f: Double = pixels[i]
            if (f < 0.0)
                f = 0.0
            if (f > 255)
                f = 255.0

            val rgb = Color(f.toInt(), f.toInt(), f.toInt(), 255).rgb
            argbArray[i] = rgb
        }
        return argbArray
    }

    fun color(): IntArray {

        val c: List<Color> = arrayListOf(Color.white, Color.GREEN)

        val argbArray = IntArray(width * height)
        for (i: Int in 0..pixels.size - 1) {
            var f: Double = pixels[i]
            if (f < 0.0)
                f = 0.0
            if (f > 255)
                f = 255.0

            val num2 = f / 255
            val num3 = (c.size - 1) * num2

            val index1: Int = num2.toInt()
            var index2 = index1 + 1
            if (index2 == c.size){
                index2 = index2 -1
            }
            var value: Double
            if (num2 > index1) {
                value = num2 - Math.floor(num2).toInt()
            } else {
                value = num3 - index1
            }
            val r = (c[index1].red * (1.0 - value) + c[index2].red * value).toInt()
            val g = (c[index1].green * (1.0 - value) + c[index2].green * value).toInt()
            val b = (c[index1].blue * (1.0 - value) + c[index2].blue * value).toInt()

            argbArray[i] = Color(r, g, b, 255).rgb
        }
        return argbArray
    }


}
