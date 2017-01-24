package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableList;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.Type;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.List;

public abstract class ValueBlock<T> extends Block {

    T value1;

    public ValueBlock(ToplevelPane pane, String fxml) {
        super(pane);
        loadFXML(fxml);
    }

    abstract T getValue(int position);

}
