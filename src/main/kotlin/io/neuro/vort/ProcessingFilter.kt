package io.neuro.vort

abstract class ProcessingFilter(fastRGB: FastImage) {

    val with = fastRGB.width
    val height = fastRGB.height

    val pixels = IntArray(with * height)

    fun setPixel(x: Int, y: Int, rgb: Int) {
        pixels.set(x + y * with, rgb)
    }

    fun getPixel(x: Int, y: Int): Int {
        return pixels[x + y * with]
    }

    fun addPixel(x: Int, y: Int, rgb: Int) {
        val pixel = getPixel(x, y)
        setPixel(x, y, pixel + rgb)
    }

    fun calculateRGB(){

    }



}
