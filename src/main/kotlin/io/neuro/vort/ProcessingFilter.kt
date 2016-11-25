package io.neuro.vort

abstract class ProcessingFilter(fastABGRImage: FastABGRImage) {

    val with = fastABGRImage.width
    val height = fastABGRImage.height

    val pixels = DoubleArray(with * height)

    fun setPixel(x: Int, y: Int, rgb: Double) {
        pixels[x + y * with] = rgb
    }

    fun getPixel(x: Int, y: Int): Double {
        return pixels[x + y * with]
    }

    fun addPixel(x: Int, y: Int, rgb: Double) {
        val pixel: Double = getPixel(x, y)
        setPixel(x, y, pixel + rgb)
    }

    fun calculateRGB(){

    }

}
