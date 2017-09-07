package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.connections.InputDot
import io.metjka.vortex.ui.connections.OutputDot
import javafx.fxml.FXML
import javafx.scene.layout.Pane

class ConstantBlock(topLevelPane: TopLevelPane) : NodeBlock(topLevelPane, ConstantBlock::class.simpleName) {

    @FXML
    lateinit var outputSpace: Pane

    val outputDot = OutputDot<Number>(this)

    init {
        outputSpace.children?.add(0, outputDot)
    }

    override fun update() {

    }

    override fun getAllInputs(): List<InputDot<*>> {
        return emptyList()
    }

    override fun getAllOutputs(): List<OutputDot<*>> {
        return listOf(outputDot)
    }

    override fun getNewCopy(): NodeBlock {
        return this
    }

}