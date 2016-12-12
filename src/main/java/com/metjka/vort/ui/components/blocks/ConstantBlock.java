package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableList;
import com.metjka.vort.ui.BlockContainer;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Optional;

public class ConstantBlock extends ValueBlock {

    public Integer value = 0;

    @FXML
    private TextField textField;

    public ConstantBlock(ToplevelPane pane) {
        super(pane, "ConstantBlock");
        textField.setText("lel");
    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }


}
