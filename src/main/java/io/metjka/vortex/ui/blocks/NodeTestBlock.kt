package io.metjka.vortex.ui.blocks

import io.metjka.vortex.precessing.FastImage
import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputDot
import io.metjka.vortex.ui.connections.OutputDot
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import java.util.*

class NodeTestBlock(topLevelPane: TopLevelPane) : NodeBlock(topLevelPane, NodeTestBlock::class.simpleName) {

    @FXML
    lateinit var inputSpace: Pane

    val inputDot = InputDot<FastImage>(this, Type.IMAGE)

    init {
        inputSpace.children.add(0, inputDot)
    }

    override fun getAllInputs(): List<InputDot<*>> {
        return emptyList()
    }

    override fun getAllOutputs(): List<OutputDot<*>> {
        return emptyList()
    }

    override fun update() {

    }

    override fun getNewCopy(): NodeBlock {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

