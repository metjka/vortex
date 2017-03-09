package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.BlockContainer

import io.metjka.vortex.ui.ComponentLoader
import io.metjka.vortex.ui.Type
import javafx.geometry.Point2D
class OutputDot<T>(block: Block, val type: Type) : ConnectionDot<T>(block), ComponentLoader {

    val typedValue: T? = null

    fun addConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasConnection(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttachmentPoint(): Point2D {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNearbyWireReaction(goodness: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getContainer(): BlockContainer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun removeConnections() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun dropConnection(connection: Connection<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}