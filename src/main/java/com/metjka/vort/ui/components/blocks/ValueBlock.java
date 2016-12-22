package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableList;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.Type;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.List;

public abstract class ValueBlock<T> extends Block implements OneOutputBlock<T> {

    T value1;

    private OutputAnchor outputAnchor;

    @FXML
    private Pane outputSpace;

    public ValueBlock(ToplevelPane pane, String fxml) {
        super(pane);
        loadFXML(fxml);
        outputAnchor = new OutputAnchor(this, 1, Type.NUMBER);
        outputSpace.getChildren().add(outputAnchor);
    }

    @Override
    public List<InputAnchor> getAllInputs() {
        return ImmutableList.of();
    }

    @Override
    public List<OutputAnchor> getAllOutputs() {
        return ImmutableList.of(outputAnchor);
    }

    @Override
    public void invalidateVisualState() {
        outputAnchor.invalidateVisualState();
    }


}
