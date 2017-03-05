package io.metjka.vortex.precessing

import java.awt.Point
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.awt.image.Raster

class FastImage {

    val pixels: IntArray?

    var height: Int
    var width: Int

    constructor() {
        val image = BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB)
        this.height = image.height
        this.width = image.width

        this.pixels = (image.raster?.dataBuffer as DataBufferInt).data

    }

    constructor(width: Int, height: Int, array: IntArray) {
        this.height = height
        this.width = width

        this.pixels = array
    }

    constructor(width: Int, height: Int) {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        this.height = image.height
        this.width = image.width

        this.pixels = (image.raster.dataBuffer as DataBufferInt).data

    }

    constructor(buf: BufferedImage) {
        val image = BufferedImage(buf.width, buf.height, BufferedImage.TYPE_INT_ARGB)
        image.graphics.drawImage(buf, 0, 0, null)
        image.graphics.dispose()

        this.pixels = (image.raster?.dataBuffer as DataBufferInt).data

        this.width = image.width
        this.height = image.height

    }


    fun getARGB(x: Int, y: Int) = pixels?.get(x + y * width)

    fun setARGB(x: Int, y: Int, valu: Int) = pixels?.set(x + y * width, valu)

    fun toBufferedImage(): BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        image.data = Raster.createRaster(image.sampleModel, DataBufferInt(pixels, pixels!!.size), Point())
        return image
    }


}
