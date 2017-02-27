package io.metjka.vortex.ui.components.blocks

import com.google.common.collect.ImmutableList
import io.metjka.vortex.ui.ToplevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.components.connections.InputAnchor
import io.metjka.vortex.ui.components.connections.OutputAnchor
import com.metjka.vort.precessing.FastImage
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

class ImageBlock(val toplevelPane: ToplevelPane) : ValueBlock<FastImage>(toplevelPane, "ImageBlock") {
    val log = KotlinLogging.logger { }

    val outputAnchor: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    val fileChooser = FileChooser()

    val extensionFilter = FileChooser.ExtensionFilter("Image (*.PNG)", "*.PNG")
    @FXML
    var fileButton: Button? = null

    @FXML
    var outputSpace: VBox? = null

    @FXML
    var imageView: ImageView? = null

    init {

        fileChooser.title = "Open PNG file!"
        fileChooser.extensionFilters.add(extensionFilter)

        outputSpace?.children?.add(outputAnchor)

        fileButton?.setOnMouseClicked {
            val file: File? = fileChooser.showOpenDialog(toplevelPane.scene.window)
            if (file != null) {
                try {
                    val bufferedImage = ImageIO.read(file)
                    val fastABGRImage = FastImage(bufferedImage)
                    value1 = fastABGRImage
                    log.info("Sent message downstream!")
                    sendUpdateDownSteam()
                    imageView?.image = SwingFXUtils.toFXImage(fastABGRImage.toBufferedImage(), null)
                } catch (ex: IOException) {
                    log.error("Read image error!", ex)
                }
            }
        }
    }

    override fun update() {
        outputAnchor.invalidateVisualState()
    }

    override fun getValue(position: Int): FastImage {
        when (position) {
            0 -> return value1
            else -> {
                throw IllegalArgumentException("Wrong position!")
            }
        }
    }

    override fun getAllOutputs(): MutableList<OutputAnchor> {
        return ImmutableList.of(outputAnchor)
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        return ImmutableList.of()
    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
