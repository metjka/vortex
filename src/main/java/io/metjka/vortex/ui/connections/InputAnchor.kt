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

class InputAnchor<T>(block: Block, val type: Type) : ConnectionAnchor(block), Target {

    @FXML
    var visibleAnchor: Shape? = null

    @FXML
    var invisibleAnchor: Shape? = null

    @FXML
    var openWire: Shape? = null

    private var connectionDep: Optional<ConnectionDep<T>>

    var errorState: BooleanProperty

    init {
        loadFXML(this::class.simpleName)

        connectionDep = Optional.empty<ConnectionDep<T>>()
        errorState = SimpleBooleanProperty(false)
        errorState.addListener { observable, oldValue, newValue -> checkError(observable, oldValue, newValue) }
    }

    fun checkError(observable: ObservableValue<out Boolean>, oldValue: Boolean?, newValue: Boolean?) {
        val style = this.visibleAnchor?.styleClass
        style?.removeAll("error")
        if (newValue!!) {
            style?.add("error")
        }
    }

    override fun getAssociatedAnchor(): ConnectionAnchor {
        return this
    }

    fun getConnection(): Optional<ConnectionDep<T>> {
        return connectionDep
    }

    fun getOppositeAnchor(): Optional<OutputAnchor<T>> {
        return this.connectionDep.map { c -> c.start }
    }

    fun setConnection(connectionDep: ConnectionDep<T>) {
        this.connectionDep = Optional.of(connectionDep)
        this.openWire?.isVisible = false
    }

    override fun removeConnections() {
        if (connectionDep.isPresent) {
            val get = connectionDep.get()
            connectionDep = Optional.empty()
            get.remove()
        }
        errorState.value = false
        openWire?.isVisible = true

        if (wireInProgress != null) {
            wireInProgress?.remove()
        }

    }

    fun onUpdate() {
        block.sendUpdateDownStream()
    }

    override fun hasConnection(): Boolean {
        return connectionDep.isPresent
    }

    override fun getAttachmentPoint(): Point2D {
        return topLevelPane.sceneToLocal(localToScene(Point2D(0.0, -7.0)))
    }

    override fun setNearbyWireReaction(goodness: Int) {

        when (goodness) {
            DrawWire.GOOD_TYPE_REACTION -> {
                this.openWire?.stroke = Color.DARKGREEN
                this.openWire?.strokeWidth = 5.0
                this.visibleAnchor?.fill = Color.DARKGREEN
            }
            DrawWire.NEUTRAL_TYPE_REACTION -> {
                this.openWire?.stroke = Color.DODGERBLUE
                this.openWire?.strokeWidth = 5.0
                this.visibleAnchor?.fill = Color.DODGERBLUE
            }
            DrawWire.WRONG_TYPE_REACTION -> {
                this.openWire?.stroke = Color.RED
                this.openWire?.strokeWidth = 3.0
                this.visibleAnchor?.fill = Color.RED
            }
            else -> {
                this.openWire?.stroke = Color.BLACK
                this.openWire?.strokeWidth = 3.0
                this.visibleAnchor?.fill = Color.BLACK
            }
        }
    }

    override fun getContainer(): BlockContainer {
        return block.getContainer()
    }

    fun invalidateVisualState() {
        this.connectionDep.ifPresent { it.invalidateVisualState() }
    }

    override fun toString(): String {
        return "InputAnchor for $block"
    }

    override fun toBundle(): Map<String, Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
