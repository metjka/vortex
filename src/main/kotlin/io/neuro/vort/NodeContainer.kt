package io.neuro.vort

import javafx.scene.layout.Pane
import javafx.scene.layout.Region

class NodeContainer : Region, Container {

    override fun asNode(): NodeContainer {
        return this
    }

    val bottomLayer: Pane
    val blockLayer: Pane
    val wireLayer: Pane

    constructor() {
        this.bottomLayer = Pane()
        this.blockLayer = Pane(bottomLayer)
        this.wireLayer = Pane(blockLayer)
        children.add(wireLayer)

        var touchContext: TouchContext = TouchContext(this)


    }

}