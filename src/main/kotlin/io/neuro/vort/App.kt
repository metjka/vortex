package io.neuro.vort

import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.awt.image.Raster
import java.io.File
import javax.imageio.ImageIO

val path = "./src/main/resources"

fun main(args: Array<String>) {
    val file: File = File(path, "test.png")
    val bufferedImage: BufferedImage = ImageIO.read(file)
    val fastABGRImage: FastABGRImage = FastABGRImage(bufferedImage)
    val pix: IntArray = processStar()

    val image = BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB)
    image.data = Raster.createRaster(image.sampleModel, DataBufferInt(pix, pix.size), Point())
    ImageIO.write(image, "PNG", File(path, "1.png"))
}

fun processStar(): IntArray {
    val star = Star(FastABGRImage(300, 300))
    star.spiralGalaxy(55, 150, 150, 4, 7, 2, 2, 2, 3, 2, 3)
    val p: IntArray = IntArray(300 * 300)
    for (i: Int in 0..star.pixels.size-1) {
        var f: Double = star.pixels[i]
        if (f < 0.0)
            f = 0.0
        if (f > Byte.MAX_VALUE)
            f = Byte.MAX_VALUE.toDouble()

        val rgb = Color(f.toInt(), f.toInt(), f.toInt(), 255).rgb
        p[i] = rgb
    }
    return p
}

private fun processGreyscale(bufferedImage: BufferedImage, fastABGRGRB: FastABGRImage): IntArray {
    val pix = IntArray(bufferedImage.width * bufferedImage.height)

    for (x: Int in 0..fastABGRGRB.width - 1) {
        for (y: Int in 0..fastABGRGRB.height - 1) {
            pix[(x + y * fastABGRGRB.width)] = grayscale2(fastABGRGRB.getRGB(x, y)!!, 10, true)
            if (y == fastABGRGRB.height) {
                println("$x $y: " + fastABGRGRB.getRGB(x, y))
            }
        }
    }
    return pix
}


fun greyscale(rgb: Int): Int {
    val c = Color(rgb)
    val red: Int = ((c.red * 0.299).toInt())
    val green: Int = ((c.green * 0.587).toInt())
    val blue: Int = ((c.blue * 0.114).toInt())
    val color = Color(red + green + blue, red + green + blue, red + green + blue, 255)
    return color.rgb
}

fun grayscale2(rgb: Int, percent: Int, brighter: Boolean): Int {
    // Use NTSC conversion formula.
    var gray = ((0.30 * (rgb shr 16 and 0xff) +
            0.59 * (rgb shr 8 and 0xff) +
            0.11 * (rgb and 0xff)) / 3).toInt()

    if (brighter) {
        gray = 255 - (255 - gray) * (100 - percent) / 100
    } else {
        gray = gray * (100 - percent) / 100
    }

    if (gray < 0) gray = 0
    if (gray > 255) gray = 255
    return rgb and 0xff000000.toInt() or (gray shl 16) or (gray shl 8) or (gray shl 0)
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

