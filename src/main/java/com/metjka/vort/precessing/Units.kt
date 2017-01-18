package com.metjka.vort.precessing

import java.util.concurrent.ThreadLocalRandom



fun ThreadLocalRandom.nextDouble(powerFrom: Int, powerTo: Int): Double {
    return this.nextDouble(powerFrom.toDouble(), powerTo.toDouble())
}