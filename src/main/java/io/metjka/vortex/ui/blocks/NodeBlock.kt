package io.metjka.vortex.ui.blocks

import io.metjka.vortex.precessing.FastImage
import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import java.util.*

class NodeBlock(topLevelPane: TopLevelPane) : Block(topLevelPane, NodeBlock::class.simpleName) {

    @FXML
    lateinit var inputSpace: Pane

    val inputDot = InputDot<FastImage>(this, Type.IMAGE)

    init {
        inputSpace.children?.add(0, inputDot)
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

