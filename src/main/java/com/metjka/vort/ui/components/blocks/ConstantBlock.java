package com.metjka.vort.ui.components.blocks;

import com.metjka.vort.ui.ToplevelPane;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Optional;

public class ConstantBlock extends ValueBlock<Integer> {

    @FXML()
    private TextField textField;

    public ConstantBlock(ToplevelPane pane) {
        super(pane, "ConstantBlock");
        textField.setText("0");

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            value = Integer.parseInt(newValue);
            updateValue();
        });

    }

    @Override
    protected Integer getValue() {
        return value;
    }

    private void updateValue() {
        this.initiateConnectionChanges();
    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }


}
