package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.loadXML
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane

class ImagePreviewWindow(image: Image) : StackPane() {

    @FXML
    var imageView: ImageView? = null

    init {
        this.loadXML("ImagePreviewWindow")
        imageView?.image = image
    }


}