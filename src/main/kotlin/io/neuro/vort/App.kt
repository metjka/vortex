package io.neuro.vort

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.WritableRaster
import java.io.File
import javax.imageio.ImageIO
import javax.swing.GrayFilter

class App {

}

val path = "C:/Users/isalnikov/Desktop/VORT/src/main/resources"


fun main(args: Array<String>) {
    var file: File = File(path, "test.png")
    val bufferedImage: BufferedImage? = ImageIO.read(file)
    val fastGRB = FastGRB(bufferedImage!!)

    val start: Long = System.currentTimeMillis()
    val rgb = fastGRB.getRGB(1, 1)
    val color = fastGRB.getColor(rgb!!)
    val end = System.currentTimeMillis()

    val pixels: ByteArray? = fastGRB.pixels

    var pix = ByteArray(fastGRB.height* fastGRB.width)

    val grayFilter = GrayFilter(false, 50)

    for (x: Int in 0..fastGRB.width - 1) {
        for (y: Int in 0..fastGRB.height - 1) {
            val filterRGB = grayFilter.filterRGB(0, 0, fastGRB.getRGB(x, y)!!)

        }
    }
//    val image = BufferedImage(fastGRB.width, fastGRB.height, BufferedImage.TYPE_INT_ARGB)
//    val data = image.data as WritableRaster
//    data.setPixels(0,0,fastGRB.width,fastGRB.height,)

    println(rgb)
    println(end - start)

    //val start: Long = System.currentTimeMillis()
    //val image = grayscale(bufferedImage)
    //val end = System.currentTimeMillis()
//
    //println(end - start)

    //ImageIO.write(image, "PNG", File(path + start.toString() + ".png"))
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

fun grayscale(image: BufferedImage?): BufferedImage? {
    val raster = image!!.raster
    val height = raster.height
    val width = raster.width

    val fastRGB = FastRGB(image)

    var array = IntArray(height * width)

    for (x: Int in 0..width - 1) {
        for (y: Int in 0..height - 1) {
            val rgb: Int = fastRGB.getRGB(x, y)


//            val red = .getRed() * 0.299
//            val green = c.getGreen() * 0.587
//            val blue = c.getBlue() * 0.114

        }
    }
    return null


}