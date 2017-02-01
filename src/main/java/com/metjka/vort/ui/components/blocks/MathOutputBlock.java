package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableList;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.Type;
import com.metjka.vort.ui.components.connections.Target;
import com.metjka.vort.ui.components.connections.ConnectionAnchor;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class MathOutputBlock extends Block implements Target {

    protected InputAnchor inputAnchor;

    @FXML
    private Pane inputSpace;

    @FXML
    private Label printValue;

    /**
     * @param pane The pane this block belongs to.
     */
    public MathOutputBlock(ToplevelPane pane) {
        super(pane);
        loadFXML("MathOutputBlock");

        inputAnchor = new InputAnchor(this, Type.NUMBER);
        inputSpace.getChildren().add(0, inputAnchor);
    }

    @Override
    public ConnectionAnchor getAssociatedAnchor() {
        return inputAnchor;
    }

    @Override
    public List<InputAnchor> getAllInputs() {
        return ImmutableList.of(inputAnchor);
    }

    @Override
    public List<OutputAnchor> getAllOutputs() {
        return ImmutableList.of();
    }

    @Override
    public void update() {
        inputAnchor.invalidateVisualState();
        if (inputAnchor.getOppositeAnchor().isPresent()) {

            OutputAnchor outputAnchor = inputAnchor.getOppositeAnchor().get();

            Block block = outputAnchor.getBlock();
            int position = outputAnchor.getPosition();

            printValue.setText(Type.NumberToString((Number) block.getValueFromBlock(position)));


        }


    }


    @Override
    public Optional<Block> getNewCopy() {
        return Optional.of(new MathOutputBlock(this.getToplevel()));
    }

}
