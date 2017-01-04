package com.metjka.vort.ui.components.blocks;

import com.metjka.vort.ui.ToplevelPane;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Optional;

public class ConstantBlock extends ValueBlock<Integer> {

    @FXML
    private TextField textField;

    public ConstantBlock(ToplevelPane pane) {
        super(pane, "ConstantBlock");

        textField.setText("0");
        value1 = 0;


        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!"".equals(newValue)) {
                value1 = Integer.parseInt(newValue);
                updateValue();
            } else {
                value1 = 0;
                updateValue();
            }
        });

    }

    protected Integer getValue() {
        return value1;
    }

    private void updateValue() {
        this.handleConnectionChanges();
    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }


    public Integer getValue1() {
        return value1;
    }
}
