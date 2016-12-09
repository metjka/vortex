package com.metjka.vort.ui;

import com.metjka.vort.ui.components.blocks.Block;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * FunctionMenu is a viskell specific menu implementation. A FunctionMenu is an
 * always present menu, once called it will remain open until the
 * {@linkplain #close() } method is called. An application can have multiple
 * instances of FunctionMenu where each menu maintains it's own state.
 * <p>
 * FunctionMenu is constructed out of three different spaces:
 * {@code searchSpace}, {@code categorySpace} and {@code utilSpace}. The
 * {@code searchSpace} should be used to display search components that can be
 * used to find stored functions more quickly, {@code categorySpace} contains an
 * {@linkplain Accordion} where each category of functions is displayed in their
 * own {@linkplain TitledPane}. {@code utilSpace} can then contain any form of
 * utility methods or components that might need quick accessing.
 * </p>
 */
public class FunctionMenu extends StackPane implements ComponentLoader {

    /**
     * The context that deals with dragging for this Menu
     */
    protected DragContext dragContext;

    /**
     * Keep track of how many blocks are placed from this menu
     */
    private int blockCounter = 0;

    private Accordion categoryContainer = new Accordion();
    private ToplevelPane parent;
    @FXML
    private Pane searchSpace;
    @FXML
    private Pane categorySpace;
    @FXML
    private Pane utilSpace;

    public FunctionMenu(boolean byMouse, ToplevelPane pane) {
        this.parent = pane;
        this.loadFXML("FunctionMenu");
        this.dragContext = new DragContext(this);

        // drop all synthesized mouse events
        categoryContainer.addEventFilter(MouseEvent.ANY, e -> {
            if (e.isSynthesized()) e.consume();
        });

        // Hiding all other categories on expanding one of them.
        List<TitledPane> allCatPanes = new ArrayList<>(categoryContainer.getPanes());
        categoryContainer.expandedPaneProperty().addListener(e -> {
            TitledPane expPane = categoryContainer.getExpandedPane();
            if (expPane != null) {
                categoryContainer.getPanes().retainAll(expPane);
            } else {
                categoryContainer.getPanes().clear();
                categoryContainer.getPanes().addAll(allCatPanes);
            }
        });

        this.categorySpace.getChildren().add(categoryContainer);

        /* Create content for utilSpace. */
        Button closeButton = new MenuButton("Close", bm -> close(bm));
        closeButton.getStyleClass().add("escape");

        utilSpace.getChildren().addAll(closeButton);

        // with an odd number of block buttons fill the last spot with a close button
        if (utilSpace.getChildren().size() % 2 == 1) {
            Button extraCloseButton = new MenuButton("Close", bm -> close(bm));
            utilSpace.getChildren().add(extraCloseButton);
        }


        for (Node button : utilSpace.getChildren()) {
            ((Region) button).setMaxWidth(Double.MAX_VALUE);
        }

        // opening animation of this menu, during which it can't be accidentally used
        this.setMouseTransparent(true);
        this.setScaleX(0.3);
        this.setScaleY(0.1);
        ScaleTransition opening = new ScaleTransition(byMouse ? Duration.ONE : Duration.millis(300), this);
        opening.setToX(1);
        opening.setToY(1);
        opening.setOnFinished(e -> this.setMouseTransparent(false));
        opening.play();
    }

    /**
     * Specialized Button that behaves better in a many touch environment.
     */
    private static class MenuButton extends Button {

        private int touchDragCounter;

        private MenuButton(String text, Consumer<Boolean> action) {
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

    private void addBlock(Block block) {
        parent.addBlock(block);
        Bounds menuBounds = this.getBoundsInParent();
        int offSetY = (this.blockCounter % 5) * 20 + (block.getAllOutputs().isEmpty() ? 250 : 125);
        if (this.localToScene(Point2D.ZERO).getX() < 200) {
            // too close to the left side of screen, put block on the right
            int offSetX = (this.blockCounter % 5) * 10 + (block.belongsOnBottom() ? 50 : 100);
            block.relocate(menuBounds.getMaxX() + offSetX, menuBounds.getMinY() + offSetY);
        } else {
            int offSetX = (this.blockCounter % 5) * 10 - (block.belongsOnBottom() ? 400 : 200);
            block.relocate(menuBounds.getMinX() + offSetX, menuBounds.getMinY() + offSetY);
        }

        if (!block.belongsOnBottom()) {
            block.refreshContainer();
        }

        block.initiateConnectionChanges();
        this.blockCounter++;
    }

    private void addDraggedBlock(TouchPoint touchPoint, Block block) {
        Point2D pos = parent.sceneToLocal(touchPoint.getSceneX(), touchPoint.getSceneY());
        parent.addBlock(block);
        block.relocate(pos.getX(), pos.getY());
        touchPoint.grab(block);
        block.initiateConnectionChanges();
    }

    /**
     * Closes this menu by removing it from it's parent.
     */
    public void close(boolean byMouse) {
        // disable it first, before removal in a closing animation
        this.setMouseTransparent(true);
        ScaleTransition closing = new ScaleTransition(byMouse ? Duration.ONE : Duration.millis(300), this);
        closing.setToX(0.3);
        closing.setToY(0.1);
        closing.setOnFinished(e -> parent.removeMenu(this));
        closing.play();
    }
}
