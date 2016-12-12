package com.metjka.vort.ui.components.blocks;

import com.metjka.vort.ui.BlockContainer;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;

import java.util.List;
import java.util.Optional;

public class ConstantBlock extends Block {
    /**
     * @param pane The pane this block belongs to.
     */
    public ConstantBlock(ToplevelPane pane) {
        super(pane);
        loadFXML("ConstantBlock");
    }

    @Override
    public List<InputAnchor> getAllInputs() {
        return null;
    }

    @Override
    public List<OutputAnchor> getAllOutputs() {
        return null;
    }

    @Override
    protected void refreshAnchorTypes() {

    }

    @Override
    public void invalidateVisualState() {

    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }
}
