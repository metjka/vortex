package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.connections.InputDot
import io.metjka.vortex.ui.connections.OutputDot
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.layout.Pane

class ValueBlock(topLevelPane: TopLevelPane) : NodeBlock(topLevelPane, ValueBlock::class.simpleName) {

    @FXML lateinit var outputSpace: Pane
    @FXML lateinit var numberInputTextField: TextField

    val outputDot = OutputDot<Number>(this)

    init {
        outputSpace.children?.add(0, outputDot)
        numberInputTextField.text = 0.toString()

        numberInputTextField.textProperty().addListener { a, b, newValue ->
            if ("" != newValue) {
                outputDot.setValue(newValue.toInt())
            }
        }
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