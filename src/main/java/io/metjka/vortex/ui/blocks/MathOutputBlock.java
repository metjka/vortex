package io.metjka.vortex.ui.blocks;

import com.google.common.collect.ImmutableList;
import io.metjka.vortex.ui.ToplevelPane;
import io.metjka.vortex.ui.connections.OutputAnchor;
import io.metjka.vortex.ui.connections.Target;
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
        super(pane, "InvertBlock");
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

            printValue.setText(block.getValueFromBlock(position).toString());


        }


    }


    @Override
    public Optional<Block> getNewCopy() {
        return Optional.of(new MathOutputBlock(this.getToplevel()));
    }

}
