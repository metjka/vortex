package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.blocks.Block
import io.metjka.vortex.ui.connections.DrawWire.*
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.paint.Color
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

    protected var connections: MutableList<Connection<T>>

    var property: SimpleObjectProperty<T>? = null

    init {
        loadFXML(this::class.simpleName)
        connections = mutableListOf()
    }

    override fun getAssociatedAnchor(): ConnectionAnchor {
        return this
    }

    fun addConnection(connection: Connection<T>) {
        connections.add(connection)
        openWire?.isVisible = false
        guardMarker?.isVisible = false
    }

     fun dropConnection(connection: Connection<T>) {
        if (this.connections.contains(connection)) {
            this.connections.remove(connection)
            this.openWire?.setVisible(!this.hasConnection())
        }
    }

    fun getOppositeAnchors(): List<InputAnchor<T>> {
        val list = ArrayList<InputAnchor<T>>()
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
        return block.topLevelPane
    }

    override fun getAttachmentPoint(): Point2D {
        return topLevelPane.sceneToLocal(localToScene(Point2D(0.0, 7.7)))
    }

    fun invalidateVisualState() {
        this.openWire?.setVisible(!this.hasConnection())
        this.guardMarker?.setVisible(false)
    }

    override fun setNearbyWireReaction(goodness: Int) {

        when (goodness) {
            GOOD_TYPE_REACTION -> {
                this.openWire?.setStroke(Color.DARKGREEN)
                this.openWire?.setStrokeWidth(5.0)
                this.visibleAnchor?.setFill(Color.DARKGREEN)
                this.guardMarker?.setStroke(Color.DARKGREEN)
            }
            NEUTRAL_TYPE_REACTION -> {
                this.openWire?.setStroke(Color.DODGERBLUE)
                this.openWire?.setStrokeWidth(5.0)
                this.visibleAnchor?.setFill(Color.DODGERBLUE)
                this.guardMarker?.setStroke(Color.DODGERBLUE)
            }
            WRONG_TYPE_REACTION -> {
                this.openWire?.setStroke(Color.RED)
                this.openWire?.setStrokeWidth(3.0)
                this.visibleAnchor?.setFill(Color.RED)
                this.guardMarker?.setStroke(Color.RED)
            }
            else -> {
                this.openWire?.setStroke(Color.BLACK)
                this.openWire?.setStrokeWidth(3.0)
                this.visibleAnchor?.setFill(Color.BLACK)
                this.guardMarker?.setStroke(Color.BLACK)
            }
        }
    }

    override fun toBundle(): Map<String, Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
