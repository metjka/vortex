package com.metjka.vort.precessing

import java.awt.Color

/**
 * Created by isalnikov on 1/30/2017.
 */
class Reverce(val fastImage: FastImage):Filter{

    val width = fastImage.width
    val height = fastImage.height

    override fun filter(): FastImage {

        val toIntArray = fastImage.pixels?.reversed()?.toIntArray()
        val image = FastImage(width, height, toIntArray!!)

        return image;
    }
}