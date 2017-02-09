package com.metjka.vort.precessing

import com.sun.javafx.util.Utils
import java.awt.Color

class HueSaturationValueFilter(val fastImage: FastImage) {

    val width = fastImage.width
    val height = fastImage.height

    fun filter(hue: Int, saturation: Int, value: Int): FastImage {
        val image = FastImage(width, height)
        for (x in 0..width - 1) {
            for (y in 0..height - 1) {
                val color = Color(fastImage.getARGB(x, y)!!)
                val r = color.red
                val g = color.green
                val b = color.blue

                val arra = floatArrayOf(0F, 0F, 0F)
                val hsb = Color.RGBtoHSB(r, g, b, arra)

                //todo fix
                val hue1 = hsb.get(0).plus((hue.toFloat() / 100F * 360F) / 100F).clampHue()
                val saturation1 = hsb.get(1).times(saturation.toFloat() / 100).clampSaturation()
                val value1 = hsb.get(2).times(value.toFloat() / 100).clampValue()

                val hsBtoRGB = Color(Color.HSBtoRGB(hue1, saturation1, value1))
                image.setARGB(x, y, hsBtoRGB.rgb)
            }
        }
        return image;
    }

    private fun Float.clampHue(): Float {
        if (this > 1) {
            return (1 - this).unaryMinus()
        } else return this
    }

    private fun Float.clampSaturation(): Float {
        if (this > 1) {
            return 1F
        } else return this
    }

    private fun Float.clampValue(): Float {
        if (this > 1) {
            return 1F
        } else return this
    }
}


