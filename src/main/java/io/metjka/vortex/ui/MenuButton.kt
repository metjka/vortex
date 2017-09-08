package io.metjka.vortex.ui

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.scene.control.Button
import javafx.util.Duration

import java.util.function.Consumer

/**
 * Specialized Button that behaves better in a many touch environment.
 */
internal class MenuButton(text: String, action: Consumer<Boolean>) : Button(text) {

    private var touchDragCounter: Int = 0

    init {
        this.touchDragCounter = 0
        this.styleClass.add("menuButton")
        this.setOnMouseClicked { event -> if (!event.isSynthesized) action.accept(true) }

        val dragReset = Timeline(KeyFrame(Duration.millis(500.0), { e -> this.touchDragCounter = 0 }, null))
        this.setOnTouchReleased { event -> if (this.touchDragCounter < 7) action.accept(false) }
        this.setOnTouchPressed { event -> dragReset.play() }
        this.setOnTouchMoved { event -> this.touchDragCounter++ }
    }
}
