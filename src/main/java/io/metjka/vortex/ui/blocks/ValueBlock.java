package io.metjka.vortex.ui.blocks;

import io.metjka.vortex.ui.ToplevelPane;

public abstract class ValueBlock<T> extends Block {

    T value1;

    public ValueBlock(ToplevelPane pane, String fxml) {
        super(pane);
        loadFXML(fxml);
    }

    abstract T getValue(int position);

}
