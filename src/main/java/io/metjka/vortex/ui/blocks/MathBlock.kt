package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputDot
import io.metjka.vortex.ui.connections.OutputDot
import javafx.fxml.FXML
import javafx.scene.layout.Pane

class MathBlock(topLevelPane: TopLevelPane) : NodeBlock(topLevelPane, MathBlock::class.simpleName) {

    @FXML lateinit var outputSpace: Pane
    @FXML lateinit var inputSpace1: Pane
    @FXML lateinit var inputSpace2: Pane

    val outputDot = OutputDot<Number>(this)
    val inputDot1 = InputDot<Number>(this, Type.NUMBER)
    val inputDot2 = InputDot<Number>(this, Type.NUMBER)

    init {
        outputSpace.children?.add(0, outputDot)
        inputSpace1.children?.add(0, inputDot1)
        inputSpace2.children?.add(0, inputDot2)

    }

    override fun update() {
        val value1 = inputDot1.connection.get().startDot?.getValue() as Int?
        val value2 = inputDot2.connection.get().startDot?.getValue() as Int?
        if ((value1 != null) && (value2 != null)) {
            val res = value1 + value2
            outputDot.setValue(res)
        }


    }

    override fun getAllInputs(): List<InputDot<*>> = emptyList()

    override fun getAllOutputs(): List<OutputDot<*>> = listOf(outputDot)

    override fun getNewCopy(): NodeBlock = this

}