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
    val fastABGRImage1 = FastABGRImage(512, 512)
    val pix: IntArray = processStar(fastABGRImage1)

    val image = BufferedImage(fastABGRImage1.width, fastABGRImage1.height, BufferedImage.TYPE_INT_ARGB)
    image.data = Raster.createRaster(image.sampleModel, DataBufferInt(pix, pix.size), Point())
    ImageIO.write(image, "PNG", File(path, "1.png"))
}

fun processStar(fastABGRImage1: FastABGRImage): IntArray {
    val star = Star(fastABGRImage1)
    //star.spiralGalaxy(200, fastABGRImage1.width / 2, fastABGRImage1.height / 2, 4, 7, 2, 4, 5, 6, 4, 5)
    star.starfield(400, 5, 10, 5, 10)

    val gray = star.color(arrayListOf(Color.CYAN, Color.BLACK))
    return gray
}

private fun processGreyscale(bufferedImage: BufferedImage, fastABGRGRB: FastABGRImage): IntArray {
    val pix = IntArray(bufferedImage.width * bufferedImage.height)

    for (x: Int in 0..fastABGRGRB.width - 1) {
        for (y: Int in 0..fastABGRGRB.height - 1) {
            pix[(x + y * fastABGRGRB.width)] = grayscale2(fastABGRGRB.getARGB(x, y), 10, true)
            if (y == fastABGRGRB.height) {
                println("$x $y: " + fastABGRGRB.getARGB(x, y))
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

