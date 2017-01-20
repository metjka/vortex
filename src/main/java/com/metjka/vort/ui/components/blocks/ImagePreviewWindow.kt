package com.metjka.vort.ui.components.blocks

import com.metjka.vort.ui.ComponentLoader
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane

class ImagePreviewWindow(image: Image) : StackPane(), ComponentLoader {

    @FXML
    var imageView: ImageView? = null

    init {
        loadFXML("ImagePreviewWindow")
        imageView?.image = image
    }


}