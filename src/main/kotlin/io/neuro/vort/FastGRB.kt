package io.neuro.vort

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte

class FastGRB(image: BufferedImage) {

    val pixels: ByteArray? = (image.raster.dataBuffer as DataBufferByte).data
    val height = image.height
    val width = image.width
    val alfa = if (image.alphaRaster != null) true else false
    val pixelLength = if (alfa) 4 else 3


    fun getRGB(x: Int, y: Int): Int? {
        var pixel: Int = getPixel(x, y)

        var argb: Int = -16777216

        if (alfa) {
            argb += (pixels?.get(pixel++)?.toInt()?.and(0xff))?.shl(24)!!
        }
        argb += (pixels?.get(pixel++)?.toInt()?.and(0xFF))?.shl(16)!!//red
        argb += (pixels?.get(pixel++)?.toInt()?.and(0xFF))?.shl(8)!! //green
        argb += (pixels?.get(pixel++)?.toInt()?.and(0xFF))!! //blue

        return argb
    }

    fun getColor(grb: Int): Color {
        val alfa = grb.shr(24).and(0xFF)
        val red = grb.shr(16).and(0xFF)
        val green = grb.shr(8).and(0xFF)
        val blue = grb.shr(0).and(0xFF)
        val color = Color(red, green, blue, alfa)
        Color(3)
        return color
    }

    fun setPixel(x: Int, y: Int, pixel: Int) {
        val originalPixel = getPixel(x, y)


    }

    private fun getPixel(x: Int, y: Int) = (y * pixelLength * width) + (x * pixelLength)

}
