package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.blocks.Block
import io.metjka.vortex.ui.connections.DrawWire.*
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Shape
import java.util.*

class OutputAnchor<T>(block: Block, val type: Type) : ConnectionAnchor(block), Target {


    @FXML
    private val visibleAnchor: Shape? = null

    @FXML
    private val invisibleAnchor: Shape? = null

    @FXML
    private val openWire: Shape? = null

    @FXML
    private val guardMarker: Shape? = null

    var connectionDeps: MutableList<ConnectionDep<T>>

    var property: Prop<T> = Prop()

    init {
        loadFXML(this::class.simpleName)
        connectionDeps = mutableListOf()
    }

    override fun getAssociatedAnchor(): ConnectionAnchor {
        return this
    }

    fun addConnection(connectionDep: ConnectionDep<T>) {
        connectionDeps.add(connectionDep)
        openWire?.isVisible = false
        guardMarker?.isVisible = false
    }

    fun dropConnection(connectionDep: ConnectionDep<T>) {
        if (this.connectionDeps.contains(connectionDep)) {
            this.connectionDeps.remove(connectionDep)
            this.openWire?.isVisible = !this.hasConnection()
        }
    }

    fun getOppositeAnchors(): List<InputAnchor<T>> {
        val list = ArrayList<InputAnchor<T>>()
        for (c in this.connectionDeps) {
            list.add(c.end)
        }
        return list
    }

    fun connectionChanged() {
        block.update()
    }

    override fun hasConnection(): Boolean {
        return connectionDeps.isNotEmpty()
    }

    override fun removeConnections() {
        while (!this.connectionDeps.isEmpty()) {
            val connection = this.connectionDeps.removeAt(0)
            connection.remove()
        }
        this.openWire?.isVisible = true
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
        this.openWire?.isVisible = !this.hasConnection()
        this.guardMarker?.isVisible = false
    }

    override fun setNearbyWireReaction(goodness: Int) {

        when (goodness) {
            GOOD_TYPE_REACTION -> {
                this.openWire?.stroke = Color.DARKGREEN
                this.openWire?.strokeWidth = 5.0
                this.visibleAnchor?.fill = Color.DARKGREEN
                this.guardMarker?.stroke = Color.DARKGREEN
            }
            NEUTRAL_TYPE_REACTION -> {
                this.openWire?.stroke = Color.DODGERBLUE
                this.openWire?.strokeWidth = 5.0
                this.visibleAnchor?.fill = Color.DODGERBLUE
                this.guardMarker?.stroke = Color.DODGERBLUE
            }
            WRONG_TYPE_REACTION -> {
                this.openWire?.stroke = Color.RED
                this.openWire?.strokeWidth = 3.0
                this.visibleAnchor?.fill = Color.RED
                this.guardMarker?.stroke = Color.RED
            }
            else -> {
                this.openWire?.stroke = Color.BLACK
                this.openWire?.strokeWidth = 3.0
                this.visibleAnchor?.fill = Color.BLACK
                this.guardMarker?.stroke = Color.BLACK
            }
        }
    }

    override fun toBundle(): Map<String, Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
