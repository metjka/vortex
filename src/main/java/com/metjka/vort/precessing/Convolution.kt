package com.metjka.vort.precessing

import java.awt.Color

class Convolution(val fastImage: FastImage) {

    val width = fastImage.width
    val height = fastImage.height

    companion object {

        val boxBlur = floatArrayOf(
                0f, 1f, 0f,
                1f, 1f, 1f,
                0f, 1f, 0f
        )
        val BOX_BLUR = Kernel(3, 3, boxBlur)

        val gaussian3Blur = floatArrayOf(
                1f, 2f, 1f,
                2f, 4f, 2f,
                1f, 2f, 1f
        )
        val GAUSSIAN3_BLUR = Kernel(3, 3, gaussian3Blur)

        val gaussian5Blur = floatArrayOf(
                0f, 0f, 1f, 0f, 0f,
                0f, 1f, 1f, 1f, 0f,
                1f, 1f, 1f, 1f, 1f,
                0f, 1f, 1f, 1f, 0f,
                0f, 0f, 1f, 0f, 0f
        )
        val GAUSSIAN5_BLUR = Kernel(5, 5, gaussian5Blur)

        val motionBlur = floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 0f, 1f
        )

        val sobelHorizontal = floatArrayOf(
                1f, 2f, 1f,
                0f, 0f, 0f,
                -1f, -2f, -1f
        )

        val sobelVertical = floatArrayOf(
                -1f, 0f, 1f,
                -2f, 0f, 2f,
                -1f, 0f, 1f
        )
        val SOBEL_HORIZONTAL = Kernel(3, 3, sobelHorizontal)
        val SOBEL_VERTICAL = Kernel(3, 3, sobelVertical)


        val kernel7 = floatArrayOf(
                0f, 0f, 0f, 5f, 0f, 0f, 0f,
                0f, 5f, 18f, 32f, 18f, 5f, 0f,
                0f, 18f, 64f, 100f, 64f, 18f, 0f,
                5f, 32f, 100f, 100f, 100f, 32f, 5f,
                0f, 18f, 64f, 100f, 63f, 18f, 0f,
                0f, 5f, 18f, 32f, 18f, 5f, 0f,
                0f, 0f, 0f, 5f, 0f, 1f, 0f
        )

        val sharpen = floatArrayOf(
                -1f, -1f, -1f,
                -1f, 9f, -1f,
                -1f, -1f, -1f
        )
        val SHARPEN = Kernel(3, 3, sharpen)

        val laplace = floatArrayOf(
                -1f, -1f, -1f,
                -1f, 8f, -1f,
                -1f, -1f, -1f
        )
        val LAPLACE = Kernel(3, 3, laplace)

        val laplaceNegative = floatArrayOf(
                1f, 1f, 1f,
                1f, -8f, 1f,
                1f, 1f, 1f
        )
        val LAPLACE_NEGATIVE = Kernel(3,3, laplaceNegative)

        val lap = floatArrayOf(
                0f, 1f, 0f,
                1f, -5f, 1f,
                0f, 1f, 0f
        )
        val LAP = Kernel(3, 3, lap)

    }

    fun convolve(kernel: Kernel): FastImage {
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

                        val color = Color(fastImage.getARGB(imageX, imageY)!!)

                        r += (color.red * kernel.getValue(filterX, filterY)).toInt()
                        g += (color.green * kernel.getValue(filterX, filterY)).toInt()
                        b += (color.blue * kernel.getValue(filterX, filterY)).toInt()

                    }
                }
                val rgb = Color(
                        (kernel.factor * r).toInt().clamp(),
                        (kernel.factor * g).toInt().clamp(),
                        (kernel.factor * b).toInt().clamp())
                        .rgb

                array[x + y * width] = rgb
            }
        }

        val fastImage1 = FastImage(width, height, array)
        return fastImage1
    }

}

