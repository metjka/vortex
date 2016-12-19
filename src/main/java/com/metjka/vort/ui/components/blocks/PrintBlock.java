package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableList;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.components.Target;
import com.metjka.vort.ui.components.connections.ConnectionAnchor;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class PrintBlock extends Block implements Target {

    protected InputAnchor inputAnchor;

    @FXML
    Pane inputSpace;

    @FXML
    Label value;

    /**
     * @param pane The pane this block belongs to.
     */
    public PrintBlock(ToplevelPane pane) {
        super(pane);
        loadFXML("OutputBlock");

        inputAnchor = new InputAnchor(this);
        inputSpace.getChildren().add(0, inputAnchor);
    }

    @Override
    public ConnectionAnchor getAssociatedAnchor() {
        return inputAnchor;
    }

    @Override
    public List<InputAnchor> getAllInputs() {
        return ImmutableList.of(inputAnchor);
    }

    @Override
    public List<OutputAnchor> getAllOutputs() {
        return ImmutableList.of();
    }

    @Override
    public void invalidateVisualState() {
        inputAnchor.invalidateVisualState();
    }

    @Override
    public Optional<Block> getNewCopy() {
        return Optional.of(new PrintBlock(this.getToplevel()));
    }
}
