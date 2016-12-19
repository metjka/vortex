package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableList;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public abstract class ValueBlock<T> extends Block {

    T value;

    private OutputAnchor output;

    @FXML
    private Pane outputSpace;

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
    public void invalidateVisualState() {
        output.invalidateVisualState();
    }

    protected abstract Integer getValue();

}
