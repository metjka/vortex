package com.metjka.vort.ui.components.blocks;

import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.Type;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;

import java.util.List;
import java.util.Optional;

public class MathBlock extends Block implements TwoOutputBlock<Integer> {

    Integer firstValue;
    Integer secondValue;

    InputAnchor inputAnchor1;
    InputAnchor inputAnchor2;

    OutputAnchor outputAnchor;

    public MathBlock(ToplevelPane pane) {
        super(pane);
        loadFXML("MathBlock");
        inputAnchor1 = new InputAnchor(this, Type.NUMBER);

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
    public void invalidateVisualState() {

    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }

    @Override
    public Integer getFirstValue() {
        return firstValue;
    }

    @Override
    public Integer getSecondValue() {
        return secondValue;
    }
}
