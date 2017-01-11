package com.metjka.vort.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.ui.ToplevelPane
import com.metjka.vort.ui.Type
import com.metjka.vort.ui.components.connections.InputAnchor
import com.metjka.vort.ui.components.connections.OutputAnchor
import io.neuro.vort.image.porcessing.FastABGRImage
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.stage.FileChooser
import tornadofx.getChildList
import java.util.*

class ImageInputBlock(val toplevelPane: ToplevelPane) : Block(toplevelPane) {

    val fastImage = FastABGRImage()

    val outputAnchore1: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    @FXML
    lateinit var fileButton: Button

    init {
        val fileChooser = FileChooser()
        val extensionFilter = FileChooser.ExtensionFilter("Image (*.PNG)", "*.PNG")
        fileChooser.title = "Open PNG file!"
        fileChooser.extensionFilters.add(extensionFilter)
        fileButton.setOnMouseClicked {
            fileChooser.showOpenDialog(toplevelPane.scene.window)
        }
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        return ImmutableList.of()
    }

    override fun getAllOutputs(): MutableList<OutputAnchor> {
        return ImmutableList.of(outputAnchore1)
    }

    override fun update() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}