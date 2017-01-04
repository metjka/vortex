package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableList;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.Type;
import com.metjka.vort.ui.components.Target;
import com.metjka.vort.ui.components.connections.ConnectionAnchor;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class PrintBlock extends Block implements Target {

    protected InputAnchor inputAnchor;

    @FXML
    private Pane inputSpace;

    @FXML
    private Label printValue;

    /**
     * @param pane The pane this block belongs to.
     */
    public PrintBlock(ToplevelPane pane) {
        super(pane);
        loadFXML("PrintBlock");

        inputAnchor = new InputAnchor(this);
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

            int position = inputAnchor.getOppositeAnchor().get().getPosition();

            switch (position) {
                case 1: {
                    Lele2(block, () -> {
                        Type type = outputAnchor.getType();
                        switch (type) {
                            case NUMBER:
                                printValue.setText(Type.NumberToString((Number) ((OneOutputBlock) block).getValue1()));
                        }
                    });
                    break;

                }

                default:
            }
        }

    }

    private void Lele2(Block block, Runnable consumer) {
        if (block instanceof OneOutputBlock) {
            consumer.run();
        } else if (block instanceof TwoOutputBlock) {
            consumer.run();
        }
    }

    @Override
    public Optional<Block> getNewCopy() {
        return Optional.of(new PrintBlock(this.getToplevel()));
    }

}
