package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableList;
import com.metjka.vort.ui.ToplevelPane;
import com.metjka.vort.ui.Type;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class MathBlock extends Block implements TwoOutputBlock<Integer> {


    private Integer firstValue;
    private Integer secondValue;

    private InputAnchor inputAnchor1;
    private InputAnchor inputAnchor2;

    private OutputAnchor outputAnchor;

    @FXML
    Pane inpitSpace;

    @FXML
    Pane outputSpace;

    public MathBlock(ToplevelPane pane) {
        super(pane);
        loadFXML("MathBlock");
        inputAnchor1 = new InputAnchor(this, Type.NUMBER);
        inputAnchor2 = new InputAnchor(this, Type.NUMBER);

        outputAnchor = new OutputAnchor(this, 1, Type.NUMBER);


    }

    @Override
    public List<InputAnchor> getAllInputs() {
        return ImmutableList.of(inputAnchor1, inputAnchor2);
    }

    @Override
    public List<OutputAnchor> getAllOutputs() {
        return ImmutableList.of(outputAnchor);
    }

    @Override
    public void update() {
        inputAnchor1.invalidateVisualState();
        inputAnchor2.invalidateVisualState();
        OutputAnchor outputAnchor1 = inputAnchor1.getOppositeAnchor().get();
        OutputAnchor outputAnchor2 = inputAnchor1.getOppositeAnchor().get();


        Block block1 = outputAnchor1.getBlock();
        int position1 = outputAnchor2.getPosition();

        Block block2 = outputAnchor1.getBlock();
        int position2 = outputAnchor2.getPosition();

        Integer inValue1;
        Integer inValue2;

        getValueFromBlock(block1, position1);
        initiateConnectionChanges();

    }

    private OneOutputBlock getValueFromBlock(Block block1, int position1) {
        switch (position1){
            case 1: {
                if (block1 instanceof OneOutputBlock){
                    return (OneOutputBlock) ((OneOutputBlock) block1).getFirstValue();
                }
            }
        }
        return null;
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
