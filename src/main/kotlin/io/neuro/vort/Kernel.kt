package io.neuro.vort

data class Kernel(val with: Int, val height: Int, val array: FloatArray) {

    val factor: Float

    init {
        factor = 1 / array.sum()
    }

    fun getValue(x: Int, y: Int): Float {
        return array[x + y * with]
    }
}