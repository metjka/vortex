package io.metjka.vortex.precessing

import java.awt.Color

class Filters(fastImage: FastImage) : ProcessingFilter(fastImage) {

    val fast = fastImage

    var bias = 0.0

    fun blur(kernel: Kernel): IntArray {
        val array = IntArray(width * height)

        for (x in 0..width - 1) {
            for (y in 0..height - 1) {

                var r = 0
                var g = 0
                var b = 0

                val offsetX = x - kernel.with / 2 + width
                val offsetY = y - kernel.height / 2 + height

                for (filterX in 0..kernel.with - 1) {
                    for (filterY in 0..kernel.height - 1) {

                        val imageX: Int = (offsetX + filterX) % width
                        val imageY: Int = (offsetY + filterY) % height

                        val color = Color(fast.getARGB(imageX, imageY)!!)

                        r += (color.red * kernel.getValue(filterX, filterY)).toInt()
                        g += (color.green * kernel.getValue(filterX, filterY)).toInt()
                        b += (color.blue * kernel.getValue(filterX, filterY)).toInt()

                    }
                }
                val rgb = Color(
                        colorFlor(colorRoof((kernel.factor * r + bias).toInt())),
                        colorFlor(colorRoof((kernel.factor * g + bias).toInt())),
                        colorFlor(colorRoof((kernel.factor * b + bias).toInt())))
                        .rgb

                array[x + y * width] = rgb
            }
        }
        return array
    }

    fun colorRoof(num: Int): Int {
        if (num > 255) {
            return 255
        }
        return num
    }

    fun colorFlor(num: Int): Int {
        if (num < 0) {
            return 0
        }
        return num
    }

}