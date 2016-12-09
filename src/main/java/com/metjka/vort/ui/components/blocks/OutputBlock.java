package com.metjka.vort.ui.components.blocks;

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

public class OutputBlock extends Block implements Target {

    protected InputAnchor inputAnchor;

    @FXML
    Pane inputSpace;

    @FXML
    Label value;

    /**
     * @param pane The pane this block belongs to.
     */
    public OutputBlock(ToplevelPane pane) {
        super(pane);
        loadFXML("OutputBlock");

        inputAnchor = new InputAnchor(this);
        inputAnchor.getChildren().add(0, inputAnchor);
    }

    @Override
    public ConnectionAnchor getAssociatedAnchor() {
        return null;
    }

    @Override
    public List<InputAnchor> getAllInputs() {
        return null;
    }

    @Override
    public List<OutputAnchor> getAllOutputs() {
        return null;
    }

    @Override
    protected void refreshAnchorTypes() {

    }

    @Override
    public void invalidateVisualState() {

    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }
}
