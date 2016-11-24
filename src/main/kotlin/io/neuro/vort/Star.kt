package io.neuro.vort

import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.fixedRateTimer

class Star(fastRGB: FastImage) {

    val fastGRB = fastRGB
    val random = ThreadLocalRandom.current()

    fun spiralGalaxy(number: Int, centerX: Int, centerY: Int, centerDensity: Int, edgeDensity: Int,
                     cycles: Int, spiralHarms: Int, powerFrom: Int, powerTo: Int, radiusFrom: Int, radiusTo: Int) {

        val num = 2.0 * Math.PI / spiralHarms

        var startAngle = 0.0

        val pix: IntArray = IntArray(fastGRB.width * fastGRB.height)

        for (i: Int in 1..spiralHarms) {
            for (j: Int in 0..number / i) {
                val power = random.nextDouble(powerFrom, powerTo)
                val radius = random.nextInt(radiusFrom, radiusTo)
                val pair: Pair<Int, Int> = spiraDistortion(centerX, centerY, centerDensity, edgeDensity, cycles, startAngle, 0.05)
                addStar(pair.first, pair.second, power, radius)
                startAngle += num
            }
        }
    }

    private fun addStar(x: Int, y: Int, power: Double, radius: Int) {
        val num1 = Math.min(Math.min(4 * (power * radius.toDouble()).toInt(), (this.fastGRB.width - 1) / 2), (this.fastGRB.height - 1) / 2)
        for (i: Int in x - num1..x + num1 - 1) {
            for (j: Int in y - num1..y + num1 - 1){

            }

        }

    }

    private fun spiraDistortion(x: Int, y: Int, centerDensity: Int, edgeDensity: Int, cycles: Int, startAngle: Double, d1: Double): Pair<Int, Int> {
        val val1 = Math.PI + cycles * 2.0
        val val2 = random.nextDouble() + val1
        val val3 = centerDensity + edgeDensity * val2
        val val4 = val3 + random.nextDouble() + (val3 * d1)
        val val5 = startAngle + val2
        val val6: Int = (val4 * Math.cos(val5)).toInt()
        val val7: Int = (val4 * Math.sin(val5)).toInt()

        val valX = seamlessCoordination(x + val6, fastGRB.width - 1)
        val valY = seamlessCoordination(y + val7, fastGRB.height - 1)

        val pair = Pair<Int, Int>(valX, valY)
        return pair
    }

    private fun seamlessCoordination(i: Int, side: Int): Int {
        var num = i % side
        if (num < 0) {
            num = side + num
        }
        return num
    }

}

private fun ThreadLocalRandom.nextDouble(powerFrom: Int, powerTo: Int): Double {
    return this.nextDouble(powerFrom.toDouble(), powerTo.toDouble())
}
