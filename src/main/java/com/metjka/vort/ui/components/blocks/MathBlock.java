package com.metjka.vort.ui.components.blocks;

import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;

import java.util.List;
import java.util.Optional;

public class MathBlock extends Block {
    /**
     * @param pane The pane this block belongs to.
     */
    public MathBlock(ToplevelPane pane) {
        super(pane);
        loadFXML("MathBlock");
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
    public void invalidateVisualState() {

    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }
}
