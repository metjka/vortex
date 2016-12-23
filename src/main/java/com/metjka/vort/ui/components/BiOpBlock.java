package com.metjka.vort.ui.components;

import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.components.blocks.Block;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class BiOpBlock extends Block {

    @FXML
    Pane bodySpace;

    InputAnchor inputAnchor1;
    InputAnchor inputAnchor2;

    OutputAnchor outputAnchor;

    public BiOpBlock(ToplevelPane pane) {
        super(pane);
        loadFXML("BiOpBlock");
    }

    @Override
    public List<InputAnchor> getAllInputs() {
        return null;
    }

    @Override
    public List<OutputAnchor> getAllOutputs() {
        return null;
    }

    protected Integer getValue() {

        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }
}
