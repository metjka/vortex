package com.metjka.vort.precessing

class ImageRotation(val fastImage: FastImage) {

    val width = fastImage.width
    val height = fastImage.height

    fun rotate(dig: Int): FastImage {
        when(dig){
            90 -> return rotate90()
            -90 -> return rotateMinus90()
            180 -> return rotate180()
            else -> throw IllegalArgumentException("Wrong angleK")
        }
    }

    fun rotate90(): FastImage {
        val image = FastImage(height, width)

        for (x in 0..height - 1) {
            for (y in 0..width - 1) {
                image.setARGB(x, y, fastImage.getARGB(width - y - 1, x)!!)
            }
        }
        return image
    }

    fun rotateMinus90(): FastImage {
        val image = FastImage(height, width)

        for (x in 0..height - 1) {
            for (y in 0..width - 1) {
                image.setARGB(x, y, fastImage.getARGB(y, height - x - 1)!!)
            }
        }
        return image
    }

    fun rotate180(): FastImage {

        val toIntArray = fastImage.pixels?.reversed()?.toIntArray()
        val image = FastImage(width, height, toIntArray!!)

        return image;
    }

}

