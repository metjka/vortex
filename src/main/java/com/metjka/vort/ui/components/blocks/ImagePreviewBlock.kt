package com.metjka.vort.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.precessing.FastImage
import com.metjka.vort.ui.ToplevelPane
import com.metjka.vort.ui.Type
import com.metjka.vort.ui.components.connections.InputAnchor
import com.metjka.vort.ui.components.connections.OutputAnchor
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane
import javafx.stage.Stage
import mu.KotlinLogging
import java.util.*

class ImagePreviewBlock(toplevelPane: ToplevelPane) : Block(toplevelPane) {

    val log = KotlinLogging.logger { }

    @FXML
    var inputSpace: Pane? = null

    @FXML
    var imageView: ImageView? = null

    val inputAnchor = InputAnchor(this, Type.IMAGE)
    var image: Image? = null


    init {
        log.info("ImagePreviewBlck creating! : {}", this.hashCode())
        loadFXML("ImagePreviewBlock")



        inputSpace?.children?.add(0, inputAnchor)
        imageView?.setOnMouseClicked {
            if (it.button == MouseButton.PRIMARY) {
                if (it.clickCount == 2) {
                    val stage: Stage = Stage()
                    val scene = Scene(ImagePreviewWindow(image!!))
                    stage.scene = scene
                    stage.show()
                }
            }

        }
    }

    override fun update() {
        inputAnchor.invalidateVisualState()

        log.info("Started update in ImagePreviewBlock: {}", hashCode())

        if (inputAnchor.oppositeAnchor.isPresent) {
            val block = inputAnchor.oppositeAnchor.get().block
            val position = inputAnchor.oppositeAnchor.get().position
            if (block is ValueBlock<*>) {
                val fastImage: FastImage? = (block.getValue(position) as FastImage);
                image = SwingFXUtils.toFXImage(fastImage?.toBufferedImage(), null)
                imageView?.image = image
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