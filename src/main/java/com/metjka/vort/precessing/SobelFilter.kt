package com.metjka.vort.precessing

import mu.KotlinLogging
import rx.Observable
import rx.Single
import rx.schedulers.Schedulers
import java.awt.Color
import java.lang.Math.sqrt

class SobelFilter(val fastImage: FastImage) : Filter {

    val log = KotlinLogging.logger { }

    val width = fastImage.width
    val height = fastImage.height

    override fun filter(): FastImage {
        val convolution = Convolution(fastImage)
        val obsSobelHorizontal = Observable.fromCallable { convolution.convolve(Convolution.SOBEL_HORIZONTAL) }
        val obsSobelVertical = Observable.fromCallable { convolution.convolve(Convolution.SOBEL_VERTICAL) }

        var fastImageSobel: FastImage? = null

        Observable.zip(obsSobelHorizontal, obsSobelVertical, { r1, r2 ->
            log.info { Thread.currentThread() }
            val fast = FastImage(width, height)
            for (x in 0..width - 1) {
                for (y in 0..height - 1) {
                    val argb1: Int? = r1.getARGB(x, y)
                    val argb2: Int? = r2.getARGB(x, y)

                    if (argb1 != null && argb2 != null) {
                        val G = sqrt(((argb1 * argb1) + (argb2 * argb2)).toDouble()).toInt().clamp()
                        fast.setARGB(x, y, Color(G).rgb)
                    }
                }
            }
            return@zip fast

        }).subscribe({ fastImageSobel = it }, { log.error("Can`t do sobel!", it) })

        return fastImageSobel!!
    }


}