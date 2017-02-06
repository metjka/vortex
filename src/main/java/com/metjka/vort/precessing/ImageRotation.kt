package com.metjka.vort.precessing

import javafx.scene.transform.Rotate

class ImageRotation(val fastImage: FastImage) : Rotator {

    val width = fastImage.width
    val height = fastImage.height

    override fun process(): FastImage {
        val image = FastImage(height, width)

        for (x in 0..height - 1) {
            for (y in 0..width - 1) {
                image.setARGB(x, y, fastImage.getARGB(width - y - 1, x)!!)
            }
        }
        return image
    }

}

