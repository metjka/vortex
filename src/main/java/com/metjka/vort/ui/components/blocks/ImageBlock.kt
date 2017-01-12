package com.metjka.vort.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.ui.ToplevelPane
import com.metjka.vort.ui.Type
import com.metjka.vort.ui.components.connections.InputAnchor
import com.metjka.vort.ui.components.connections.OutputAnchor
import io.neuro.vort.image.porcessing.FastABGRImage
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import mu.KotlinLogging
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

class ImageBlock(val toplevelPane: ToplevelPane) : Block(toplevelPane) {

    val log = KotlinLogging.logger { }

    val outputAnchore1: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)
    val fileChooser = FileChooser()
    val extensionFilter = FileChooser.ExtensionFilter("Image (*.PNG)", "*.PNG")

    @FXML
    var fileButton: Button? = null

    @FXML
    var outputSpace: VBox? = null

    @FXML
    var imageView: ImageView? = null

    init {
        loadFXML("ImageBlock")

        fileChooser.title = "Open PNG file!"
        fileChooser.extensionFilters.add(extensionFilter)

        outputSpace?.children?.add(0, outputAnchore1)

        fileButton?.setOnMouseClicked {
            val file: File? = fileChooser.showOpenDialog(toplevelPane.scene.window)
            try {
                val bufferedImage = ImageIO.read(file)
                val fastABGRImage = FastABGRImage(bufferedImage)
                imageView?.image = SwingFXUtils.toFXImage(fastABGRImage.image, null)
            } catch (ex: IOException) {
                log.error("Read image error!", ex)
            }
        }
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        return ImmutableList.of()
    }

    override fun getAllOutputs(): MutableList<OutputAnchor> {
        return ImmutableList.of(outputAnchore1)
    }

    override fun update() {

    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}