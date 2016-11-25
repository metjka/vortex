package io.neuro.vort

import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte

class FastABGRImage {

    var image: BufferedImage
    val pixels: ByteArray?

    var height: Int
    var width: Int

    val alfa: Boolean
    val pixelLength: Int

    constructor() {
        this.image = BufferedImage(512, 512, BufferedImage.TYPE_4BYTE_ABGR)
        this.height = image.height
        this.width = image.width
        this.pixels = (image.raster?.dataBuffer as DataBufferByte).data

        this.alfa = if (image.alphaRaster != null) true else false
        this.pixelLength = if (alfa) 4 else 3
    }

    constructor(width: Int, height: Int) {
        this.image = BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR)
        this.pixels = (image.raster.dataBuffer as DataBufferByte).data
        this.height = image.height
        this.width = image.width

        this.alfa = if (image.alphaRaster != null) true else false
        this.pixelLength = if (alfa) 4 else 3
    }

    constructor(buf: BufferedImage) {
        this.image = buf
        this.pixels = (image.raster?.dataBuffer as DataBufferByte).data
        this.height = image.height
        this.width = image.width

        this.alfa = if (image.alphaRaster != null) true else false
        this.pixelLength = if (alfa) 4 else 3
    }

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

    private fun getPixel(x: Int, y: Int) = pixelLength * (x + y * width)

}
