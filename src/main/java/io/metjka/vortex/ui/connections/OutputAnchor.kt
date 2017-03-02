package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.blocks.Block
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.shape.Shape
import java.util.ArrayList

class OutputAnchor<T>(block: Block, val type: Type) : ConnectionAnchor(block), Target {


    @FXML
    private val visibleAnchor: Shape? = null

    @FXML
    private val invisibleAnchor: Shape? = null

    @FXML
    private val openWire: Shape? = null

    @FXML
    private val guardMarker: Shape? = null

    protected var connections: MutableList<Connection>

    var value: T? = null

    init {
        loadFXML(this::class.simpleName)
        connections = mutableListOf()
    }

    override fun getAssociatedAnchor(): ConnectionAnchor {
        return this
    }

    fun addConnection(connection: Connection) {
        connections.add(connection)
        openWire?.isVisible = false
        guardMarker?.isVisible = false
    }

    protected fun dropConnection(connection: Connection) {
        if (this.connections.contains(connection)) {
            this.connections.remove(connection)
            this.openWire?.setVisible(!this.hasConnection())
        }
    }

    fun getOppositeAnchors(): List<InputAnchor> {
        val list = ArrayList<InputAnchor>()
        for (c in this.connections) {
            list.add(c.end)
        }
        return list
    }

    override fun hasConnection(): Boolean {
        return connections.isNotEmpty()
    }

    override fun removeConnections() {
        while (!this.connections.isEmpty()) {
            val connection = this.connections.removeAt(0)
            connection.remove()
        }
        this.openWire?.setVisible(true)
        if (this.wireInProgress != null) {
            this.wireInProgress!!.remove()
        }
    }



    override fun getContainer(): BlockContainer {
        return block.toplevelPane
    }

    override fun getAttachmentPoint(): Point2D {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNearbyWireReaction(goodness: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toBundle(): Map<String, Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
