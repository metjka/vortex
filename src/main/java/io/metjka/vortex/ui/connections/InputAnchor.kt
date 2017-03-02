package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.blocks.Block
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Shape
import java.util.*

class InputAnchor(block: Block, val type: Type) : ConnectionAnchor(block), Target {

    @FXML
    var visibleAnchor: Shape? = null

    @FXML
    var invisibleAnchor: Shape? = null

    @FXML
    var openWire: Shape? = null

    private var connection: Optional<Connection>

    var errorState: BooleanProperty

    init {
        loadFXML(this::class.simpleName)

        connection = Optional.empty<Connection>()
        errorState = SimpleBooleanProperty(false)
        errorState.addListener { observable, oldValue, newValue -> checkError(observable, oldValue, newValue) }
    }

    fun checkError(observable: ObservableValue<out Boolean>, oldValue: Boolean?, newValue: Boolean?) {
        val style = this.visibleAnchor?.getStyleClass()
        style?.removeAll("error")
        if (newValue!!) {
            style?.add("error")
        }
    }

    override fun getAssociatedAnchor(): ConnectionAnchor {
        return this
    }

    fun getConnection(): Optional<Connection> {
        return connection
    }

    fun getOppositeAnchor(): Optional<OutputAnchor<*>> {
        return this.connection.map { c -> c.start }
    }

    protected fun setConnection(connection: Connection) {
        this.connection = Optional.of(connection)
        this.openWire?.setVisible(false)
    }

    override fun removeConnections() {
        if (connection.isPresent) {
            val get = connection.get()
            get.remove()

            connection = Optional.empty()
        }
        errorState.value = false
        openWire?.isVisible = true

        if (wireInProgress != null) {
            wireInProgress?.remove()
        }

    }

    override fun hasConnection(): Boolean {
        return connection.isPresent
    }

    override fun getAttachmentPoint(): Point2D {
        return topLevelPane.sceneToLocal(localToScene(Point2D(0.0, -7.0)))
    }

    override fun setNearbyWireReaction(goodness: Int) {

        when (goodness) {
            DrawWire.GOOD_TYPE_REACTION -> {
                this.openWire?.setStroke(Color.DARKGREEN)
                this.openWire?.setStrokeWidth(5.0)
                this.visibleAnchor?.setFill(Color.DARKGREEN)
            }
            DrawWire.NEUTRAL_TYPE_REACTION -> {
                this.openWire?.setStroke(Color.DODGERBLUE)
                this.openWire?.setStrokeWidth(5.0)
                this.visibleAnchor?.setFill(Color.DODGERBLUE)
            }
            DrawWire.WRONG_TYPE_REACTION -> {
                this.openWire?.setStroke(Color.RED)
                this.openWire?.setStrokeWidth(3.0)
                this.visibleAnchor?.setFill(Color.RED)
            }
            else -> {
                this.openWire?.setStroke(Color.BLACK)
                this.openWire?.setStrokeWidth(3.0)
                this.visibleAnchor?.setFill(Color.BLACK)
            }
        }
    }

    override fun getContainer(): BlockContainer {
        return block.getContainer()
    }

    fun invalidateVisualState() {
        this.connection.ifPresent { connection -> connection.invalidateVisualState() }
    }

    override fun toBundle(): Map<String, Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toString(): String {
        return "InputAnchor for $block"
    }
}
