package io.metjka.vortex.ui.blocks

import io.metjka.vortex.precessing.FastImage
import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.ComponentLoader
import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.shape.Circle
import javafx.scene.shape.CubicCurve
import javafx.scene.transform.Transform
import mu.KotlinLogging
import java.util.*

class NodeBlock(topLevelPane: TopLevelPane) : Block(topLevelPane, NodeBlock::class.simpleName) {

    @FXML
    var inputSpace: Pane? = null

    val inputDot = InputDot<FastImage>(this, Type.IMAGE)

    init {
        inputSpace?.children?.add(0, inputDot)
    }


    override fun getAllInputs(): List<InputAnchor<*>> {
        return emptyList()
    }

    override fun getAllOutputs(): List<OutputAnchor<*>> {
        return emptyList()
    }

    override fun update() {

    }

    override fun getNewCopy(): Optional<Block> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class InputDot<T>(block: Block, image: Type) : ConnectionDot<T>(block), ComponentLoader {

    @FXML
    var circle: Circle? = null

    var connection: Connection<T>? = null

    init {
        loadFXML("InputDot")

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
        if (connection != null) {
            val con: Connection<T> = connection as Connection<T>
            connection == null
            con.remove()
        }
    }

    fun onUpdate() {
        block.update()
    }

}

class OutputDot<T>(block: Block, val type: Type) : ConnectionDot<T>(block), ComponentLoader {

    val typedValue: T? = null

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

abstract class ConnectionDot<X>(val block: Block) : Circle() {

    val log = KotlinLogging.logger { }

    var connection: Optional<Connection<*>> = Optional.empty()
    var wireInProgress: Boolean = false

    val topLevelPane = block.topLevelPane

    init {
        addEventHandler(MouseEvent.MOUSE_PRESSED, { handleMousePress(it) })
        addEventHandler(MouseEvent.MOUSE_DRAGGED, { handleMouseDragged(it) })
        addEventHandler(MouseEvent.MOUSE_RELEASED, { handleMouseReleased(it) })
    }

    companion object {
        fun <X> initDraw(connectionDot: ConnectionDot<X>): Connection<*> {
            val log = KotlinLogging.logger { }
            when (connectionDot) {
                is InputDot<*> -> {
                    log.info("Drawing from input dot!")

                    if (connectionDot.hasConnection()) {
                        connectionDot.removeConnections()
                    }

                    return Connection<X>(connectionDot as InputDot<X>)
                }
                is OutputAnchor<*> -> {
                    log.info("Drawing from output dot!")

                    return Connection<X>(connectionDot as OutputDot<X>)
                }
                else -> throw IllegalArgumentException("Wrong connection type!")
            }
        }
    }

    private fun handleMousePress(mouseEvent: MouseEvent?) {

        wireInProgress = true

        if (!wireInProgress && !mouseEvent?.isSynthesized!!) {
            connection = Optional.of(initDraw(this))

        }
        mouseEvent?.consume()
    }

    private fun handleMouseReleased(mouseEvent: MouseEvent?) {
        wireInProgress = false

        connection.if
    }

    private fun handleMouseDragged(mouseEvent: MouseEvent?) {
        if (!wireInProgress) {

        }
    }


    abstract fun removeConnections()
    abstract fun hasConnection(): Boolean
    abstract fun getAttachmentPoint(): Point2D
    abstract fun setNearbyWireReaction(goodness: Int)
    abstract fun getContainer(): BlockContainer

}

class Connection<T> private constructor() : CubicCurve(), ChangeListener<Transform> {

    var start: OutputDot<T>? = null
    var end: InputDot<T>? = null

    init {

    }

    constructor(start: OutputDot<T>) : this() {
        start.addConnection()
        start.localToSceneTransformProperty().addListener(this)

    }

    constructor(end: InputDot<T>) : this() {

    }

    fun remove() {
        start?.localToSceneTransformProperty()?.removeListener(this)
        end?.localToSceneTransformProperty()?.removeListener(this)

        start?.dropConnection(this)
        end?.removeConnections()
        start?.topLevelPane?.removeConnection(this)

        end?.onUpdate()
    }

    override fun changed(observable: ObservableValue<out Transform>?, oldValue: Transform?, newValue: Transform?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}

