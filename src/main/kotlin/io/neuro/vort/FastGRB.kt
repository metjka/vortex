package io.neuro.vort

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte

class FastGRB(image: BufferedImage) {

    val pixels: ByteArray? = (image.raster.dataBuffer as DataBufferByte).data
    val height = image.height - 1
    val width = image.width - 1
    val alfa: Boolean = if (image.alphaRaster != null) true else false
    val pixelLength = if (alfa) 4 else 3


    fun getRGB(x: Int, y: Int): Int? {

        var pixel: Int = getPixel(x, y)

        var argb: Int = 0
        argb += -16777216

        if (alfa) {
            argb += (pixels?.get(pixel++)?.toInt()?.and(0xff))?.shl(24)!!
        }

        val blue: Int = (pixels?.get(pixel++)?.toInt()?.and(0xFF))?.shl(0)!!//blue
        val green: Int = (pixels?.get(pixel++)?.toInt()?.and(0xFF))?.shl(8)!!//green
        val red: Int = (pixels?.get(pixel++)?.toInt()?.and(0xFF))?.shl(16)!!//red

        argb += green
        argb += red
        argb += blue

        return argb
    }

    fun getColor(rgb: Int): Color {
        val color = Color(rgb, alfa)
        return color
    }

    fun setPixel(x: Int, y: Int, pixel: Int) {
        val originalPixel = getPixel(x, y)


    }

    private fun getPixel(x: Int, y: Int) = pixelLength * (x + y * width)

}
