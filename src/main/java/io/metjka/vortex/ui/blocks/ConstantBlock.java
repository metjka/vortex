package io.metjka.vortex.ui.blocks;

import com.google.common.collect.ImmutableList;
import io.metjka.vortex.ui.ToplevelPane;
import io.metjka.vortex.ui.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class ConstantBlock extends ValueBlock<Integer> {

    private final OutputAnchor outputAnchor = new OutputAnchor(this, 0, Type.NUMBER);
    @FXML
    private TextField textField;

    @FXML
    private Pane outputSpace;

    public ConstantBlock(ToplevelPane pane) {
        super(pane, "ConstantBlock");

        outputSpace.getChildren().add(0, outputAnchor);

        textField.setText("0");
        value1 = 0;

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!"".equals(newValue)) {
                value1 = Integer.parseInt(newValue);
                sendUpdateDownSteam();
            } else {
                value1 = 0;
                sendUpdateDownSteam();
            }
        });

    }

    @Override
    public void update() {
        outputAnchor.invalidateVisualState();
    }

    @Override
    public List<OutputAnchor> getAllOutputs() {
        return ImmutableList.of(outputAnchor);
    }

    @Override
    public List<InputAnchor> getAllInputs() {
        return ImmutableList.of();
    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }

    @Override
    Integer getValue(int position) {
        return value1;
    }
}