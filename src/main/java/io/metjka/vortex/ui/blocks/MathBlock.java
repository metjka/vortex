package io.metjka.vortex.ui.blocks;

import com.google.common.collect.ImmutableList;
import io.metjka.vortex.ui.ToplevelPane;
import io.metjka.vortex.ui.connections.OutputAnchor;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class MathBlock extends ValueBlock<Integer> {

    private static final Integer DEFAULT_VALUE = 0;

    enum Method {
        ADD, EXT, MIX, MIN, MAX, MULL
    }

    private Integer inValue1;
    private Integer inValue2;

    private InputAnchor inputAnchor1 = new InputAnchor(this, Type.NUMBER);
    private InputAnchor inputAnchor2 = new InputAnchor(this, Type.NUMBER);
    private OutputAnchor outputAnchor = new OutputAnchor(this, 0, Type.NUMBER);

    @FXML
    private Pane inputSpace1;

    @FXML
    private Pane inputSpace2;

    @FXML
    private Pane outputSpace;

    @FXML
    private TextField textField;

    @FXML
    private ComboBox<String> methodComboBox;

    private Method method;

    public MathBlock(ToplevelPane pane) {
        super(pane, "MathBlock");
        value1 = DEFAULT_VALUE;

        inputSpace1.getChildren().add(0, inputAnchor1);
        inputSpace2.getChildren().add(0, inputAnchor2);

        outputSpace.getChildren().add(0, outputAnchor);

        methodComboBox.getSelectionModel().select(0);
        method = Method.ADD;

        methodComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            method = getMethod(newValue);
            update();
        });

    }

    private Method getMethod(String methodString) {

        switch (methodString) {
            case "ADD":
                return Method.ADD;
            case "EXT":
                return Method.EXT;
            case "MIX":
                return Method.MIX;
            case "MIN":
                return Method.MIN;
            case "MAX":
                return Method.MAX;
            case "MULL":
                return Method.MULL;
        }
        throw new IllegalArgumentException("Wrong method name!");
    }

    private int calculate(Integer a, Integer b) {
        switch (method) {
            case ADD:
                return a + b;
            case EXT:
                return a - b;
            case MIX:
                return (a + b) / 2;
            case MIN:
                return Math.min(a, b);
            case MAX:
                return Math.max(a, b);
            case MULL:
                return a * b;
        }
        return 0;
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

        if (inputAnchor1.getOppositeAnchor().isPresent() && inputAnchor2.getOppositeAnchor().isPresent()) {
            OutputAnchor outputAnchor1 = inputAnchor1.getOppositeAnchor().get();
            OutputAnchor outputAnchor2 = inputAnchor2.getOppositeAnchor().get();

            Block block1 = outputAnchor1.getBlock();
            int position1 = outputAnchor2.getPosition();

            Block block2 = outputAnchor2.getBlock();
            int position2 = outputAnchor2.getPosition();

            inValue1 = (Integer) block1.getValueFromBlock(position1);
            inValue2 = (Integer) block2.getValueFromBlock(position2);

            if (inValue1 != null && inValue2 != null) {
                value1 = calculate(inValue1, inValue2);
            } else {
                value1 = DEFAULT_VALUE;
            }

            textField.setText(value1.toString());

            sendUpdateDownSteam();
        }

    }

    @Override
    Integer getValue(int position) {
        switch (position) {
            case 0:
                return value1;
            default:
                throw new IllegalArgumentException("Wrong position!");
        }

    }

    @Override
    public Optional<Block> getNewCopy() {
        return null;
    }

    public Integer getValue1() {
        return value1;
    }

}
