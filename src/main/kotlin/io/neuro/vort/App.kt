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
    val file: File = File(path, "red.png")
    val bufferedImage: BufferedImage? = ImageIO.read(file)
    val fastGRB = FastGRB(bufferedImage!!)

    val pix = IntArray(bufferedImage.width * bufferedImage.height)

    for (x: Int in 0..fastGRB.width  -1) {
        for (y: Int in 0..fastGRB.height -1) {
            pix[(x + y * fastGRB.width)] = grayscale(fastGRB.getRGB(x, y)!!)
            if (y == fastGRB.height) {
                println("$x $y: "+fastGRB.getRGB(x, y))
            }
        }
    }

    val image = BufferedImage(fastGRB.width, fastGRB.height, BufferedImage.TYPE_INT_ARGB)
    image.data = Raster.createRaster(image.sampleModel, DataBufferInt(pix, pix.size ), Point())
    ImageIO.write(image, "PNG", File(path, "1.png"))

}

fun grayscale(rgb: Int): Int {
    val c = Color(rgb)
    val red: Int = ((c.red * 0.299).toInt())
    val green: Int = ((c.green * 0.587).toInt())
    val blue: Int = ((c.blue * 0.114).toInt())
    val color = Color(red + green + blue, red + green + blue, red + green + blue, 255)
    return color.rgb
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

