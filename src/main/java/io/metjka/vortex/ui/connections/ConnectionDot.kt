package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.NodeBlockContainer
import io.metjka.vortex.ui.blocks.NodeBlock
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Circle
import mu.KotlinLogging
import java.util.*

abstract class ConnectionDot(val block: NodeBlock) : Circle() {

    val log = KotlinLogging.logger { }

    var connection: Optional<Connection> = Optional.empty()
    var connectionDrawer: Optional<ConnectionDrawer> = Optional.empty()
    var wireInProgress: Boolean = false

    val topLevelPane = block.topLevelPane

    init {
        addEventHandler(MouseEvent.MOUSE_PRESSED, {
            handleMousePress(it)
            it.consume()
        })
        addEventHandler(MouseEvent.MOUSE_DRAGGED, {
            handleMouseDragged(it)
            it.consume()
        })
        addEventHandler(MouseEvent.MOUSE_RELEASED, {
            handleMouseReleased(it)
            it.consume()
        })
    }

    companion object {
        fun initDraw(connectionDot: ConnectionDot): ConnectionDrawer {
            val log = KotlinLogging.logger { }
            when {
                (connectionDot is InputDot<*>) and connectionDot.hasConnection() -> {
                    (connectionDot as InputDot<*>).let {
                        log.info("Drawing from input dot!")

                        if (connectionDot.hasConnection()) {
                            connectionDot.removeConnections()
                        }

                        return ConnectionDrawer(connectionDot, connectionDot.topLevelPane)
                    }
                }
                (connectionDot is OutputDot<*>) and connectionDot.hasConnection() -> {
                    (connectionDot as OutputDot<*>).let {
                        log.info("Drawing from output dot!")

                        connectionDot.removeConnections()

                        return ConnectionDrawer(connectionDot, connectionDot.topLevelPane)
                    }

                }
                connectionDot is InputDot<*> -> {
                    log.info("Drawing from input dot!")

                    if (connectionDot.hasConnection()) {
                        connectionDot.removeConnections()
                    }

                    return ConnectionDrawer(connectionDot, connectionDot.topLevelPane)
                }
                connectionDot is OutputDot<*> -> {
                    log.info("Drawing from output dot!")

                    return ConnectionDrawer(connectionDot, connectionDot.topLevelPane)
                }
                else -> {
                    throw IllegalArgumentException("Unknown behavior!")
                }
            }
        }
    }

    private fun handleMousePress(mouseEvent: MouseEvent) {
        wireInProgress = true
        if (!mouseEvent.isSynthesized) {
            connectionDrawer = Optional.of(initDraw(this))
        }
        mouseEvent.consume()
    }

    private fun handleMouseDragged(mouseEvent: MouseEvent) {
        if (connectionDrawer.isPresent && wireInProgress) {
            val point2D: Point2D? = topLevelPane.sceneToLocal(mouseEvent.sceneX, mouseEvent.sceneY)
            val drawer = connectionDrawer.get()
            drawer.move(point2D)
        }
    }

    private fun handleMouseReleased(mouseEvent: MouseEvent) {
        wireInProgress = false
        if (connectionDrawer.isPresent) {
            val drawer = connectionDrawer.get()
            drawer.handleRelease(mouseEvent)
        }
    }

    abstract fun removeConnections()
    abstract fun hasConnection(): Boolean
    abstract fun getAttachmentPoint(): Point2D
    abstract fun getContainer(): NodeBlockContainer

}