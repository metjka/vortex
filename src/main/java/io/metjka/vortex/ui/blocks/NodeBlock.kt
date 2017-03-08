package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.ComponentLoader
import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Circle
import javafx.scene.shape.CubicCurve
import java.util.*

class NodeBlock(topLevelPane: TopLevelPane) : Block(topLevelPane, NodeBlock::class.simpleName) {

    var inputImage = InputDot()


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

class InputDot<T> : ConnectionDot(), ComponentLoader {

}

class OutputDot<T> : ConnectionDot(), ComponentLoader {

}

open class ConnectionDot : Circle() {

    var connectionDrawer = ConnectionDrawer()
    var wireInProgress: Boolean = false

    init {
        addEventHandler(MouseEvent.MOUSE_PRESSED, { handleMousePress(it) })
        addEventHandler(MouseEvent.MOUSE_DRAGGED, { handleMouseDragged(it) })
        addEventHandler(MouseEvent.MOUSE_RELEASED, { handleMouseReleased(it) })
    }

    private fun handleMouseReleased(mouseEvent: MouseEvent?) {
        wireInProgress = false
    }

    private fun handleMouseDragged(mouseEvent: MouseEvent?) {
        if (!wireInProgress) {

        }
    }

    private fun handleMousePress(mouseEvent: MouseEvent?) {

        if (!wireInProgress && !mouseEvent?.isSynthesized!!) {
            initDraw(this)
        }
        mouseEvent?.consume()
    }

    fun initDraw(connectionDot: ConnectionDot) {
        when(connectionDot){
            is InputDot<*> -> {}
            is OutputAnchor<*> -> {}
            else ->  throw IllegalArgumentException("Wrong ")
        }
    }
}


class ConnectionDrawer : CubicCurve() {
    f
}