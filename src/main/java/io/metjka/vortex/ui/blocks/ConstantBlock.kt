package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import java.util.*

class ConstantBlock(topLevelPane: TopLevelPane) : Block(topLevelPane, ConstantBlock::class.simpleName) {

    @FXML
    private val textField: TextField? = null

    @FXML
    private val outputSpace: Pane? = null

    val outputAnchor: OutputAnchor<Int> = OutputAnchor(this, Type.NUMBER)

    init {
        outputSpace?.children?.add(0, outputAnchor)
        textField?.text = "0"
        outputAnchor.property?.value = 0

        outputAnchor.property?.addListener { _ ->
            update()
        }

        textField?.textProperty()?.addListener { _, _, newValue ->
            if ("" != newValue) {
                outputAnchor.property?.value = Integer.parseInt(newValue)
            } else {
                outputAnchor.property?.value = 0
            }
        }

    }

    override fun getAllInputs(): List<InputAnchor<*>> {
        return emptyList()
    }

    override fun getAllOutputs(): List<OutputAnchor<*>> {
        return listOf(outputAnchor)
    }

    override fun update() {
        outputAnchor.invalidateVisualState()

        sendUpdateDownStream()

    }

    override fun getNewCopy(): Optional<Block> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}