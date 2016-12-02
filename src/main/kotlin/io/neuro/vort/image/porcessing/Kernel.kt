package io.neuro.vort.image.porcessing

data class Kernel(val with: Int, val height: Int, val array: FloatArray) {

    val factor: Float

    init {
        val sum = array.sum()

        if (sum.toInt() == 0) {
            factor = 1f
        } else {
            factor = 1f / sum
        }
    }

    fun getValue(x: Int, y: Int): Float {
        return array[x + y * with]
    }
}