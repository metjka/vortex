package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.ComponentLoader
import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.blocks.Block
import io.metjka.vortex.ui.serialize.Bundleable
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane

abstract class ConnectionAnchor(val block: Block) : StackPane(), ComponentLoader, Bundleable {

     var wireInProgress: DrawWire? = null
    protected var eventRedirectionTarget: DrawWire? = null

     val topLevelPane: TopLevelPane = block.topLevelPane

    init {
        addEventHandler(MouseEvent.MOUSE_PRESSED, { handleMousePress(it) })
        addEventHandler(MouseEvent.MOUSE_DRAGGED, {
            if (this.eventRedirectionTarget != null && !it.isSynthesized()) {
                this.eventRedirectionTarget!!.handleMouseDrag(it)
            }
            it.consume()
        })

        addEventHandler(MouseEvent.MOUSE_RELEASED) {
            if (this.eventRedirectionTarget != null && !it.isSynthesized) {
                this.eventRedirectionTarget!!.handleMouseRelease(it)
                this.eventRedirectionTarget = null
            }
            it.consume()
        }
    }

    abstract fun removeConnections()
    abstract fun hasConnection(): Boolean
    abstract fun getAttachmentPoint(): Point2D
    abstract fun setNearbyWireReaction(goodness: Int)
    abstract fun getContainer(): BlockContainer

    fun handleChange(){
        block.update()
    }

    private fun handleMousePress(event: MouseEvent) {
        if (this.wireInProgress == null && this.eventRedirectionTarget == null && !event.isSynthesized) {
            this.eventRedirectionTarget = DrawWire.initiate(this, null)
        }
        event.consume()
    }

    override fun toString(): String {
        val simpleName = this::class.simpleName
        return "$simpleName belong to $block"
    }



}