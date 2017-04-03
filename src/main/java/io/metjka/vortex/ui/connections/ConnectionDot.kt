package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.NodeBlockContainer
import io.metjka.vortex.ui.blocks.NodeBlock
import io.metjka.vortex.ui.connections.Connection
import io.metjka.vortex.ui.connections.InputDot
import io.metjka.vortex.ui.connections.OutputDot
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Circle
import mu.KotlinLogging
import java.util.*

abstract class ConnectionDot<X>(val block: NodeBlock) : Circle() {

    val log = KotlinLogging.logger { }

    var connection: Optional<Connection<X>> = Optional.empty()
    var connectionDrawer: Optional<ConnectionDrawer> = Optional.empty()
    var wireInProgress: Boolean = false

    val topLevelPane = block.topLevelPane

    init {
        addEventHandler(MouseEvent.MOUSE_PRESSED, { handleMousePress(it) })
        addEventHandler(MouseEvent.MOUSE_DRAGGED, { handleMouseDragged(it) })
        addEventHandler(MouseEvent.MOUSE_RELEASED, { handleMouseReleased(it) })
    }

    companion object {
        fun <X> initDraw(connectionDot: ConnectionDot<X>): ConnectionDrawer {
            val log = KotlinLogging.logger { }
            when (connectionDot) {
                is InputDot<*> -> {
                    log.info("Drawing from input dot!")

                    if (connectionDot.hasConnection()) {
                        connectionDot.removeConnections()
                    }

                    return ConnectionDrawer(connectionDot as InputDot<X>, connectionDot.topLevelPane)
                }
                is OutputDot<*> -> {
                    log.info("Drawing from output dot!")

                    return ConnectionDrawer(connectionDot as OutputDot<X>, connectionDot.topLevelPane)
                }
                else -> throw IllegalArgumentException("Wrong connection type!")
            }
        }
    }

    private fun handleMousePress(mouseEvent: MouseEvent?) {

        wireInProgress = true

        if (!mouseEvent?.isSynthesized!!) {
            connectionDrawer = Optional.of(initDraw(this))

        }
        mouseEvent.consume()
    }

    private fun handleMouseDragged(mouseEvent: MouseEvent?) {
        connection.ifPresent {
            connection.get().setFreePosition(mouseEvent!!)
        }
    }


    private fun handleMouseReleased(mouseEvent: MouseEvent?) {
        wireInProgress = false

        connection.ifPresent {
            connection.get().remove()
        }
    }


    abstract fun removeConnections()
    abstract fun hasConnection(): Boolean
    abstract fun getAttachmentPoint(): Point2D
    abstract fun getContainer(): NodeBlockContainer

}