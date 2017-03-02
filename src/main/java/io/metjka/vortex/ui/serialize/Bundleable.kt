package io.metjka.vortex.ui.serialize

interface Bundleable {

    fun toBundle(): Map<String, Any>

}