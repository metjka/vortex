package com.metjka.vort.ui;

import com.metjka.vort.ui.blocks.Block;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.input.TouchPoint;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Optional;
import java.util.function.Consumer;

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

    public FunctionMenu(boolean byMouse, HaskellCatalog catalog, ToplevelPane pane) {
        this.parent = pane;
        this.dragContext = new DragContext(this);

        /* Create content for utilSpace. */
        Button closeButton = new MenuButton("Close", bm -> close(bm));
        closeButton.getStyleClass().add("escape");
        Button valBlockButton = new MenuButton("Constant", bm -> addConstantBlock());
        Button arbBlockButton = new MenuButton("Arbitrary", bm -> addBlock(new ArbitraryBlock(parent)));
        Button disBlockButton = new MenuButton("Observe", bm -> addBlock(new DisplayBlock(parent)));
        Button lambdaBlockButton = new MenuButton("Lambda", bm -> addLambdaBlock());
        Button choiceBlockButton = new MenuButton("Choice", bm -> addChoiceBlock());
        Button applyBlockButton = new MenuButton("Apply", bm -> addBlock(new FunApplyBlock(parent, new ApplyAnchor(1))));


        utilSpace.getChildren().addAll(closeButton, disBlockButton, arbBlockButton, valBlockButton, lambdaBlockButton, applyBlockButton, choiceBlockButton);


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

    private void addConstantBlock() {
        ConstantBlock val = new ConstantBlock(this.parent);
        addBlock(val);
        val.editValue(Optional.of("\"Hello, World!\""));
    }

    private void addLambdaBlock() {
        addBlock(new LambdaBlock(this.parent, 1));
    }

    private void addChoiceBlock() {
        ChoiceBlock def = new ChoiceBlock(this.parent);
        addBlock(def);
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
