package io.metjka.vortex.precessing

fun Int.clamp(): Int {
    when {
        this > 255 -> return 255
        this < 0 -> return 0
        else -> return this
    }
}
