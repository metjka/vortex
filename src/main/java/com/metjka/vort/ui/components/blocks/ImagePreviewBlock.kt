package com.metjka.vort.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.precessing.FastImage
import com.metjka.vort.ui.ToplevelPane
import com.metjka.vort.ui.Type
import com.metjka.vort.ui.components.connections.InputAnchor
import com.metjka.vort.ui.components.connections.OutputAnchor
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import mu.KotlinLogging
import java.util.*

class ImagePreviewBlock(toplevelPane: ToplevelPane) : Block(toplevelPane) {

    val log = KotlinLogging.logger { }

    @FXML
    var inputSpace: Pane? = null

    @FXML
    var imageView: ImageView? = null

    val inputAnchor = InputAnchor(this, Type.IMAGE)

    init {
        loadFXML("ImagePreviewBlock")
        inputSpace?.children?.add(0, inputAnchor)
        log.info("ImagePreview block was created: {}", this.hashCode())
    }

    override fun update() {
            inputAnchor.invalidateVisualState()

        log.info("Started update in ImagePreviewBlock: {}", hashCode())

        if (inputAnchor.oppositeAnchor.isPresent) {
            val block = inputAnchor.oppositeAnchor.get().block
            val position = inputAnchor.oppositeAnchor.get().position
            if (block is ValueBlock<*>) {
                val fastImage: FastImage? = (block.getValue(position) as FastImage);
                imageView?.image = SwingFXUtils.toFXImage(fastImage?.toBufferedImage(), null)
            }
        }
    }

    override fun getAllOutputs(): MutableList<OutputAnchor> {
        return ImmutableList.of()
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        return ImmutableList.of(inputAnchor)
    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}