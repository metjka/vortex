package com.metjka.vort.precessing

import java.awt.Color
import java.awt.Point
import java.awt.event.KeyListener
import java.awt.image.BufferedImage
import java.awt.image.ConvolveOp
import java.awt.image.DataBufferInt
import java.awt.image.Raster
import java.io.File
import javax.imageio.ImageIO

val path = "./src/main/resources"

val easyBlur = floatArrayOf(
        .0f, .2f, .0f,
        .2f, .2f, .2f,
        .0f, .2f, .0f
)

val hardBlur = floatArrayOf(
        0f, 0f, 1f, 0f, 0f,
        0f, 1f, 1f, 1f, 0f,
        1f, 1f, 1f, 1f, 1f,
        0f, 1f, 1f, 1f, 0f,
        0f, 0f, 1f, 0f, 0f
)

val hardMotionBlur = floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f,
        0f, 0f, 0f, 0f, 1f
)

val kernel7 = floatArrayOf(
        0f, 0f, 0f, 5f, 0f, 0f, 0f,
        0f, 5f, 18f, 32f, 18f, 5f, 0f,
        0f, 18f, 64f, 100f, 64f, 18f, 0f,
        5f, 32f, 100f, 100f, 100f, 32f, 5f,
        0f, 18f, 64f, 100f, 63f, 18f, 0f,
        0f, 5f, 18f, 32f, 18f, 5f, 0f,
        0f, 0f, 0f, 5f, 0f, 1f, 0f
)

val sharpen = floatArrayOf(
        -1f, -1f, -1f,
        -1f, 9f, -1f,
        -1f, -1f, -1f
)

val laplace = floatArrayOf(
        -1f, -1f, -1f,
        -1f, 8f, -1f,
        -1f, -1f, -1f
)

val lap  = floatArrayOf(
        0f, 1f, 0f,
        1f, -5f, 1f,
        0f, 1f, 0f

)

val kernel = Kernel(3, 3, easyBlur)
val kernel2 = Kernel(5, 5, hardBlur)
val kernel3 = Kernel(5, 5, hardMotionBlur)
val kernelq = Kernel(7, 7, kernel7)
val kernleSharpen = Kernel(3, 3, sharpen)
val kernelLaplace = Kernel(3, 3, laplace)

fun main(args: Array<String>) {
    val file: File = File(path, "te/lena.png")
    val bufferedImage: BufferedImage = ImageIO.read(file)
    val fastImage: FastImage = FastImage(bufferedImage)

    val dest = BufferedImage(fastImage.width, fastImage.height, BufferedImage.TYPE_3BYTE_BGR)
    val convolveOp = ConvolveOp(java.awt.image.Kernel(3, 3, hardMotionBlur))
    val startOP = System.currentTimeMillis()
    val filter: BufferedImage = convolveOp.filter(bufferedImage, dest)
    val endOP = System.currentTimeMillis()
    ImageIO.write(filter, "PNG" , File(path, "te/base.png"))

    val image = BufferedImage(fastImage.width, fastImage.height, BufferedImage.TYPE_INT_ARGB)

    val startMY = System.currentTimeMillis()
    val pix: IntArray = blur(fastImage)
    val endMY = System.currentTimeMillis()
    image.data = Raster.createRaster(image.sampleModel, DataBufferInt(pix, pix.size), Point())
    ImageIO.write(image, "PNG", File(path, "te/5.png"))

    println("op")
    println(endOP - startOP)
    println("my")
    println(endMY - startMY)
}

fun blur(fastABGRGRB: FastImage): IntArray {
    val blur = Filters(fastABGRGRB)
    val blur1 = blur.blur(kernel2)
    return blur1
}

fun processStar(fastImage1: FastImage): IntArray {
    val star = Star(fastImage1)
    star.spiralGalaxy(200, fastImage1.width / 2, fastImage1.height / 2, 4, 7, 2, 4, 5, 6, 4, 5)
    star.starfield(400, 5, 10, 5, 10)

    val gray = star.color(arrayListOf(Color.CYAN, Color.BLACK))
    return gray
}

private fun processGreyscale(bufferedImage: BufferedImage, fastABGRGRB: FastImage): IntArray {
    val pix = IntArray(bufferedImage.width * bufferedImage.height)

    for (x: Int in 0..fastABGRGRB.width - 1) {
        for (y: Int in 0..fastABGRGRB.height - 1) {
            pix[(x + y * fastABGRGRB.width)] = grayscale2(fastABGRGRB.getARGB(x, y)!!, 10, true)
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

fun lence(size: Int): BufferedImage {
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
