package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputDot
import io.metjka.vortex.ui.connections.OutputDot
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.layout.Pane

class ResultBlock(topLevelPane: TopLevelPane) : NodeBlock(topLevelPane, ResultBlock::class.simpleName) {

    @FXML lateinit var inputSpace: Pane
    @FXML lateinit var numberInputTextField: TextField

    val inputDot = InputDot<Number>(this, Type.NUMBER)

    init {
        inputSpace.children?.add(0, inputDot)
        numberInputTextField.text = 0.toString()
    }

    override fun update() {
        val opositeConnectionDot = inputDot.getOpositeConnectionDot()
        val value = opositeConnectionDot?.getValue() as Number
        numberInputTextField.text = value.toString()
    }

    override fun getAllInputs(): List<InputDot<*>> = listOf(inputDot)

    override fun getAllOutputs(): List<OutputDot<*>> = emptyList()

    override fun getNewCopy(): NodeBlock = this

}