package com.metjka.vort.ui;

import com.metjka.vort.ui.components.blocks.Block;
import com.metjka.vort.ui.components.connections.Connection;
import com.metjka.vort.ui.components.connections.DrawWire;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

/**
 * A menu for attaching something to an open wire.
 */
public class WireMenu extends TilePane {

    /**
     * The toplevel pane this menu is on.
     */
    private final ToplevelPane toplevel;

    /**
     * The draw wire belonging to this menu
     */
    private DrawWire attachedWire;

    public WireMenu(DrawWire wire, boolean byMouse) {
        this.toplevel = wire.getAnchor().getPane();
        this.attachedWire = wire;
        this.setMouseTransparent(true);

        this.getStyleClass().add("menu");
        this.setPrefColumns(1);
        this.setVgap(5);

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("escape");
        cancelButton.setOnAction(event -> this.attachedWire.remove());
        // FIXME: silly workaround, somehow the buttons in this menu don't work on touch click while those in FunctionMenu do...
        cancelButton.setOnTouchPressed(event -> this.attachedWire.remove());

        if (wire.getAnchor() instanceof InputAnchor) {

            Button arbitraryBlockButton = new Button("Arbitrary");
            arbitraryBlockButton.setOnAction(event -> addBlockWithOutput(new ArbitraryBlock(this.toplevel)));
            arbitraryBlockButton.setOnTouchPressed(event -> addBlockWithOutput(new ArbitraryBlock(this.toplevel)));

            Button rationalBlockButton = new Button("Rational");
            rationalBlockButton.setOnAction(event -> addBlockWithOutput(new SliderBlock(this.toplevel, false)));
            rationalBlockButton.setOnTouchPressed(event -> addBlockWithOutput(new SliderBlock(this.toplevel, false)));

            Button IntegerBlockButton = new Button("Integer");
            IntegerBlockButton.setOnAction(event -> addBlockWithOutput(new SliderBlock(this.toplevel, true)));
            IntegerBlockButton.setOnTouchPressed(event -> addBlockWithOutput(new SliderBlock(this.toplevel, true)));

            this.getChildren().addAll(cancelButton, arbitraryBlockButton, rationalBlockButton, IntegerBlockButton);

        } else {

            Button disBlockButton = new Button("Observe");
            disBlockButton.setOnAction(event -> addBlockWithInput(new DisplayBlock(this.toplevel)));
            disBlockButton.setOnTouchPressed(event -> addBlockWithInput(new DisplayBlock(this.toplevel)));

            this.getChildren().addAll(cancelButton, disBlockButton);
        }

        // opening animation
        this.setScaleX(0.1);
        this.setScaleY(0.1);
        ScaleTransition opening = new ScaleTransition(byMouse ? Duration.ONE : Duration.millis(250), this);
        opening.setToX(1);
        opening.setToY(1);
        opening.setOnFinished(e -> this.setMouseTransparent(false));
        opening.play();
    }

    private void addBlockWithInput(Block block) {
        this.toplevel.addBlock(block);
        block.relocate(this.attachedWire.getEndX(), this.attachedWire.getEndY());
        this.close();

        if (!block.belongsOnBottom()) {
            block.refreshContainer();
        }

        block.initiateConnectionChanges();
        InputAnchor input = block.getAllInputs().get(0);
        Connection connection = this.attachedWire.buildConnectionTo(input);
        if (connection != null) {
            connection.getStartAnchor().initiateConnectionChanges();
        }
        this.attachedWire.remove();
    }

    private void addBlockWithOutput(Block block) {
        this.toplevel.addBlock(block);
        block.relocate(this.attachedWire.getStartX(), this.attachedWire.getStartY() - block.prefHeight(-1));
        this.close();

        if (!block.belongsOnBottom()) {
            block.refreshContainer();
        }

        block.initiateConnectionChanges();
        OutputAnchor output = block.getAllOutputs().get(0);
        Connection connection = this.attachedWire.buildConnectionTo(output);
        if (connection != null) {
            connection.getStartAnchor().initiateConnectionChanges();
        }
        this.attachedWire.remove();
    }

    /**
     * Closes this menu by removing it from it's parent.
     */
    public void close() {
        this.toplevel.removeMenu(this);
    }

}
