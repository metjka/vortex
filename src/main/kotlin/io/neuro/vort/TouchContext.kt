package io.neuro.vort

import javafx.scene.input.MouseEvent

class TouchContext {

    var stage: Container;

    constructor(nodeNodeContainer: NodeContainer) {
        this.stage = nodeNodeContainer

        stage.asNode().addEventHandler(MouseEvent.MOUSE_PRESSED, { handleMousePress(it) })
        stage.asNode().addEventHandler(MouseEvent.MOUSE_RELEASED, { handleMouseRelease(it) })

    }

    private fun handleMouseRelease(e: MouseEvent) {
        println(e.eventType)
    }

    private fun handleMousePress(e: MouseEvent) {
        println(e.eventType)
    }


}