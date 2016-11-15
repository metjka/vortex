package io.neuro.vort

import com.metjka.vort.FastRGB
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

val path = ".\\src\\io.neuro.vort.main\\resources\\"

fun main(args: Array<String>) {
    var file: File = File(path + "test.png")
    val bufferedImage: BufferedImage? = ImageIO.read(File(path + "test.png"))

    val start: Long = System.currentTimeMillis()
    val image = grayscale(bufferedImage)
    val end = System.currentTimeMillis()

    println(end - start)

    ImageIO.write(image, "PNG", File(path + start.toString() + ".png"))
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