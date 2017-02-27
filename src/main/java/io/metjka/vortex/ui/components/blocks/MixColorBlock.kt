package io.metjka.vortex.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.precessing.FastImage
import io.metjka.vortex.ui.ToplevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.components.connections.InputAnchor
import io.metjka.vortex.ui.components.connections.OutputAnchor
import javafx.fxml.FXML
import javafx.scene.layout.VBox
import java.util.*

class MixColorBlock(toplevelPane: ToplevelPane) : ValueBlock<FastImage>(toplevelPane, "MixColorBlock") {

    //todo

    val inputAnchor: InputAnchor = InputAnchor(this, Type.IMAGE)
    val outputAnchor: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    @FXML
    var inputSpace: VBox? = null

    @FXML
    var outputSpace: VBox? = null


    init {
        inputSpace?.children?.add(0, inputAnchor)
        outputSpace?.children?.add(outputAnchor)

    }

    override fun update() {

    }

    override fun getValue(position: Int): FastImage {
        when (position) {
            0 -> return value1
            else -> throw IllegalArgumentException("Wrong position!")
        }
    }


    override fun getAllOutputs(): MutableList<OutputAnchor> {
        return ImmutableList.of(outputAnchor)
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        return ImmutableList.of(inputAnchor)
    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
