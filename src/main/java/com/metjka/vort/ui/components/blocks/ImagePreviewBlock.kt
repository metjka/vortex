package com.metjka.vort.ui.components.blocks

import com.metjka.vort.ui.ToplevelPane
import com.metjka.vort.ui.Type
import com.metjka.vort.ui.components.connections.InputAnchor
import com.metjka.vort.ui.components.connections.OutputAnchor
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import java.util.*

class ImagePreviewBlock(toplevelPane: ToplevelPane) : Block(toplevelPane) {


    @FXML
    var inputSpace: Pane? = null

    val inputAncore = InputAnchor(this, Type.IMAGE)

    init {
        loadFXML("ImagePreviewBlock")



    }


    override fun getAllOutputs(): MutableList<OutputAnchor> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}