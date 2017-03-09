package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Circle
import mu.KotlinLogging
import java.util.*

abstract class ConnectionDot<X>(val block: Block) : Circle() {

    val log = KotlinLogging.logger { }

    var connection: Optional<Connection<X>> = Optional.empty()
    var wireInProgress: Boolean = false

    val topLevelPane = block.topLevelPane

    init {
        addEventHandler(MouseEvent.MOUSE_PRESSED, { handleMousePress(it) })
        addEventHandler(MouseEvent.MOUSE_DRAGGED, { handleMouseDragged(it) })
        addEventHandler(MouseEvent.MOUSE_RELEASED, { handleMouseReleased(it) })
    }

    companion object {
        fun <X> initDraw(connectionDot: ConnectionDot<X>): Connection<X> {
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
            connection = Optional.of(ConnectionDot.initDraw(this))

        }
        mouseEvent?.consume()
    }

    private fun handleMouseReleased(mouseEvent: MouseEvent?) {
        wireInProgress = false

        connection.ifPresent {

        }
    }

    private fun handleMouseDragged(mouseEvent: MouseEvent?) {
        if (!wireInProgress) {
            connection.ifPresent {
                connection.get().setFreePosition(mouseEvent!!)
            }
        }
    }


    abstract fun removeConnections()
    abstract fun hasConnection(): Boolean
    abstract fun getAttachmentPoint(): Point2D
    abstract fun setNearbyWireReaction(goodness: Int)
    abstract fun getContainer(): BlockContainer

}