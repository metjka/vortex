package io.metjka.vortex.ui.blocks

import com.google.common.collect.ImmutableList
import io.metjka.vortex.precessing.FastImage
import io.metjka.vortex.ui.ToplevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane
import javafx.stage.FileChooser
import javafx.stage.Stage
import mu.KotlinLogging
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class ImageOutputBlock(toplevelPane: ToplevelPane) : Block(toplevelPane, "InvertBlock") {

    val log = KotlinLogging.logger { }

    val inputAnchor = InputAnchor(this, Type.IMAGE)
    var image: Image? = null
    val extensionFilter = FileChooser.ExtensionFilter("Image (*.PNG)", "*.PNG")

    @FXML
    var inputSpace: Pane? = null

    @FXML
    var imageView: ImageView? = null

    @FXML
    var buttonSave: Button? = null


    init {
        log.info("ImageOutputBlock creating! : {}", this.hashCode())
        loadFXML("ImageOutputBlock")

        buttonSave?.setOnMouseClicked {
            if (image != null) {
                val fileChooser = FileChooser()

                fileChooser.title = "Open PNG file!"
                fileChooser.extensionFilters.add(extensionFilter)

                val file: File? = fileChooser.showSaveDialog(toplevelPane.scene.window)

                file?.let {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file)
                }
            }
        }

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

        log.info("Started update in ImageOutputBlock: {}", hashCode())

        if (inputAnchor.oppositeAnchor.isPresent) {
            val block = inputAnchor.oppositeAnchor.get().block
            val position = inputAnchor.oppositeAnchor.get().position
            if (block is ValueBlock<*>) {
                val fastImage: FastImage? = (block.getValue(position) as FastImage);
                val toBufferedImage = fastImage?.toBufferedImage()
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
        throw UnsupportedOperationException("not implemented")
    }

}