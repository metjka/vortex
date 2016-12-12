package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableList;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public abstract class ValueBlock extends Block {

    private OutputAnchor output;

    @FXML
    Pane outputSpace;

    /**
     * @param pane The pane this block belongs to.
     */
    public ValueBlock(ToplevelPane pane, String fxml) {
        super(pane);
        loadFXML(fxml);
        output = new OutputAnchor(this);
        outputSpace.getChildren().add(output);

    }

    @Override
    public List<InputAnchor> getAllInputs() {
        return ImmutableList.of();
    }

    @Override
    public List<OutputAnchor> getAllOutputs() {
        return ImmutableList.of(output);
    }

    @Override
    protected void refreshAnchorTypes() {

    }

    @Override
    public void invalidateVisualState() {

    }

}
