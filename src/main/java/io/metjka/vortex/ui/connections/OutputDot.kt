package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.ComponentLoader
import io.metjka.vortex.ui.NodeBlockContainer
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.blocks.NodeBlock
import javafx.geometry.Point2D
import java.util.*

class OutputDot<T>(block: NodeBlock, val type: Type) : ConnectionDot<T>(block), ComponentLoader {

    val typedValue: T? = null

    val connections: MutableList<Connection<T>> = mutableListOf()

    fun getOppositeConnectionDots(): ArrayList<InputDot<T>> {
        val list = ArrayList<InputDot<T>>()
        for (c in this.connections) {
            c.endDot?.let {
                list.add(it)
            }
        }
        return list
    }

    fun addConnection(connection: Connection<T>) {
        connections.add(connection)
    }

    fun dropConnection(connection: Connection<T>) {
        if (this.connections.contains(connection)) {
            this.connections.remove(connection)
        }
    }

    override fun hasConnection(): Boolean {
        return connections.isNotEmpty()
    }

    override fun getAttachmentPoint(): Point2D {
        return topLevelPane.sceneToLocal(localToScene(Point2D(0.0, 0.0)))
    }

    override fun getContainer(): NodeBlockContainer {
        return block.topLevelPane
    }

    fun onUpdate() {
        block.update()
    }

    override fun removeConnections() {
        while (this.connections.isNotEmpty()) {
            val connection = this.connections.removeAt(0)
            connection.remove()
        }
    }


}