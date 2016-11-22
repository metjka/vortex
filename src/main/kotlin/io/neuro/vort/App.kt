package io.neuro.vort

import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.awt.image.Raster
import java.io.File
import javax.imageio.ImageIO
import javax.swing.GrayFilter


val path = "./src/main/resources"


fun main(args: Array<String>) {
    var file: File = File(path, "test.png")
    val bufferedImage: BufferedImage? = ImageIO.read(file)
    val fastGRB = FastGRB(bufferedImage!!)

    val start: Long = System.currentTimeMillis()
    val rgb = fastGRB.getRGB(0, 0)
    val color = fastGRB.getColor(rgb!!)
    val end = System.currentTimeMillis()

    val pixels: ByteArray? = fastGRB.pixels

    val pix = IntArray(fastGRB.height * fastGRB.width)

    val grayFilter = GrayFilter(true, 50)

    for (x: Int in 0..fastGRB.width - 1) {
        for (y: Int in 0..fastGRB.height - 1) {
            pix[(x * y)] = grayscale(fastGRB.getRGB(x, y)!!)
        }
    }

    val image = BufferedImage(fastGRB.width, fastGRB.height, BufferedImage.TYPE_INT_ARGB)
    val image2 = BufferedImage(fastGRB.width,fastGRB.height, BufferedImage.TYPE_4BYTE_ABGR)
    image2.data = Raster.createRaster(image2.sampleModel,DataBufferByte(fastGRB.pixels!!, fastGRB.pixels.size), Point())
    image.data = Raster.createRaster(image.sampleModel, DataBufferInt(pix, pix.size+1), Point())
    ImageIO.write(image, "PNG", File(path, "kurwa.png"))
    ImageIO.write(image2,"PNG", File(path, "pls.png"))
    println(end-start)

}

fun lense(size: Int): BufferedImage {
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val center = size / 2
    for (x: Int in 0..size - 1) {
        for (y: Int in 0..size - 1) {
            val dx = center - x
            val dy = center - y
            val power: Double = Math.sqrt((dx * dx + dy * dy).toDouble()) / center
            if (power <= 1) {
                val col = Color((power * 255).toInt(), (power * 255).toInt(), (power * 255).toInt(), 255 - (power * 255).toInt())
                image.setRGB(x, y, col.rgb)
            }

        }
    }
    return image
}

fun xor(size: Int): BufferedImage {
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    for (x: Int in 0..size - 1) {
        for (y: Int in 0..size - 1) {
            val unit = x.xor(y)
            val r = 255 - unit
            val g = unit
            val b = unit % 128
            val color = Color(r, g, b)
            image.setRGB(x, y, color.rgb)
        }
    }
    return image
}

fun grayscale(rgb: Int): Int {
    val c = Color(rgb)
    val red = (c.getRed() * 0.299).toInt()
    val green = (c.getGreen() * 0.587).toInt()
    val blue = (c.getBlue() * 0.114).toInt()

    return Color(red, green,blue,255).rgb
}