package io.metjka.vortex.precessing

import mu.KotlinLogging
import rx.Observable
import java.awt.Color

class SobelFilter(val fastImage: FastImage) {

    val log = KotlinLogging.logger { }

    val width = fastImage.width
    val height = fastImage.height

    fun filter(): Observable<FastImage> {
        val convolution = Convolution(fastImage)
        val obsSobelHorizontal = Observable.fromCallable { convolution.convolve(Convolution.SOBEL_HORIZONTAL) }
        val obsSobelVertical = Observable.fromCallable { convolution.convolve(Convolution.SOBEL_VERTICAL) }

        val zip = Observable.zip(obsSobelHorizontal, obsSobelVertical, { r1, r2 ->
            val fast = FastImage(width, height)
            for (x in 0..width - 1) {
                for (y in 0..height - 1) {
                    val argb1: Int? = r1.getARGB(x, y)
                    val argb2: Int? = r2.getARGB(x, y)
                    if (argb1 != null && argb2 != null) {

                        val cx = Color(argb1)
                        val cy = Color(argb2)

                        val rx = cx.red
                        val gx = cx.green
                        val bx = cx.blue

                        val ry = cy.red
                        val gy = cy.green
                        val by = cy.blue

                        val R = Math.hypot(rx.toDouble(), ry.toDouble()).toInt().clamp()
                        val G = Math.hypot(gx.toDouble(), gy.toDouble()).toInt().clamp()
                        val B = Math.hypot(bx.toDouble(), by.toDouble()).toInt().clamp()

                        val color = Color(R, G, B)

                        fast.setARGB(x, y, color.rgb)
                    }
                }
            }
            return@zip fast

        })

        return zip
    }


}