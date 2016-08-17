import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.Raster
import java.io.File
import javax.imageio.ImageIO

val path = ".\\src\\main\\resources\\"

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    val lense = lense(600)
    val end = System.currentTimeMillis()

    println(end - start)

    ImageIO.write(lense, "PNG", File(path + start.toString() + ".png"))
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