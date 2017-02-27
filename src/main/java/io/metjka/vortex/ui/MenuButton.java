package io.metjka.vortex.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * Specialized Button that behaves better in a many touch environment.
 */
class MenuButton extends Button {

    private int touchDragCounter;

    MenuButton(String text, Consumer<Boolean> action) {
        super(text);
        this.touchDragCounter = 0;
        this.getStyleClass().add("menuButton");
        this.setOnMouseClicked(event -> {
            if (!event.isSynthesized()) action.accept(true);
        });

        Timeline dragReset = new Timeline(new KeyFrame(Duration.millis(500), e -> this.touchDragCounter = 0));
        this.setOnTouchReleased(event -> {
            if (this.touchDragCounter < 7) action.accept(false);
        });
        this.setOnTouchPressed(event -> dragReset.play());
        this.setOnTouchMoved(event -> this.touchDragCounter++);
    }
}
