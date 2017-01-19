package com.metjka.vort.ui.components.blocks

import com.metjka.vort.ui.ComponentLoader
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ImagePreviewWindow(image: Image) : Parent(), ComponentLoader {

    @FXML
    var imageView: ImageView? = null

    init {
        loadFXML("ImagePreviewWindow")
        if (image != null) {
            imageView?.image = image
        }

    }


}