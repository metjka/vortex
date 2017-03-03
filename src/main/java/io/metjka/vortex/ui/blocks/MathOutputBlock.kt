package io.metjka.vortex.ui.blocks;

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import java.util.*

class MathOutputBlock(topLevelPane: TopLevelPane) : Block(topLevelPane, MathOutputBlock::class.simpleName) {

    val inputAnchor: InputAnchor<Int> = InputAnchor<Int>(this, Type.NUMBER);

    @FXML
    val inputSpace: Pane? = null

    @FXML
    val printValue: Label? = null

    /**
     * @param pane The pane this block belongs to.
     */
   init {
        inputSpace?.getChildren()?.add(0, inputAnchor);

    }

    override fun getAllInputs():List<InputAnchor<*>> {
        return listOf(inputAnchor)
    }

    override fun getAllOutputs():List<OutputAnchor<*>> {
        return emptyList()
    }

    override fun update() {
        inputAnchor.invalidateVisualState()

        val property: Int = inputAnchor.getOppositeAnchor().get().property?.value!!
        printValue?.text = property.toString()

    }

    override fun getNewCopy(): Optional<Block> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




}
