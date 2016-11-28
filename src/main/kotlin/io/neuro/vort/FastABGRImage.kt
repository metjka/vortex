package io.neuro.vort

import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt

class FastABGRImage {

    var image: BufferedImage
    val pixels: IntArray?

    var height: Int
    var width: Int


    constructor() {
        this.image = BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB)
        this.height = image.height
        this.width = image.width

        this.pixels = (image.raster?.dataBuffer as DataBufferInt).data

    }

    constructor(width: Int, height: Int) {
        this.image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        this.height = image.height
        this.width = image.width

        this.pixels = (image.raster.dataBuffer as DataBufferInt).data

    }

    constructor(buf: BufferedImage) {
        this.image = BufferedImage(buf.width, buf.height, BufferedImage.TYPE_INT_ARGB)
        image.graphics.drawImage(buf, 0, 0, null)
        image.graphics.dispose()

        this.pixels = (image.raster?.dataBuffer as DataBufferInt).data

        this.height = image.height
        this.width = image.width

    }

     fun getARGB(x: Int, y: Int) = x + y * width

}
