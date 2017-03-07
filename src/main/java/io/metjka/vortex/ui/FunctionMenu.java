package io.metjka.vortex.ui;

import io.metjka.vortex.ui.blocks.*;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class FunctionMenu extends StackPane implements ComponentLoader {

    /**
     * The context that deals with dragging for this Menu
     */
    protected DragContext dragContext;

    /**
     * Keep track of how many blocks are placed from this menu
     */
    private int blockCounter = 0;

    private Accordion categoryContainer = new Accordion();
    private TopLevelPane parent;
    @FXML
    private Pane searchSpace;
    @FXML
    private Pane categorySpace;
    @FXML
    private Pane utilSpace;

    public FunctionMenu(boolean byMouse, TopLevelPane pane) {
        this.parent = pane;
        this.loadFXML("FunctionMenu");
        this.dragContext = new DragContext(this);

        // drop all synthesized mouse events
        categoryContainer.addEventFilter(MouseEvent.ANY, e -> {
            if (e.isSynthesized()) e.consume();
        });

        // Hiding all other categories on expanding one of them.
        List<TitledPane> allCatPanes = new ArrayList<>(categoryContainer.getPanes());
        categoryContainer.expandedPaneProperty().addListener(e -> {
            TitledPane expPane = categoryContainer.getExpandedPane();
            if (expPane != null) {
                categoryContainer.getPanes().retainAll(expPane);
            } else {
                categoryContainer.getPanes().clear();
                categoryContainer.getPanes().addAll(allCatPanes);
            }
        });

        this.categorySpace.getChildren().add(categoryContainer);

        /* Create content for utilSpace. */
        Button closeButton = new MenuButton("Close", bm -> close(bm));
        closeButton.getStyleClass().add("escape");

        Button outputBlock = new MenuButton("Output", bm -> addBlock(new MathOutputBlock(parent)));
        Button mathBlock = new MenuButton("Math", bm -> addBlock(new MathBlock(parent)));
        Button constantBlock = new MenuButton("Constant", bm -> addBlock(new ConstantBlock(parent)));
        Button node = new MenuButton("Node", bm -> addBlock(new NodeBlock(parent)));
//        Button imageBlock = new MenuButton("Image", bm -> addBlock(new ImageBlock(parent)));
//        Button convolutionBlock = new MenuButton("Convolution", bm -> addBlock(new ConvolutionBlock(parent)));
//        Button imagePreviewBlock = new MenuButton("Image Output", bm -> addBlock(new ImageOutputBlock(parent)));
//        Button invertBlock = new MenuButton("Invert", bm -> addBlock(new InvertBlock(parent)));
//        Button greyscaleBlock = new MenuButton("Greyscale", bm -> addBlock(new GrayScaleBlock(parent)));
//        Button sobelBlock = new MenuButton("Sobel", bm -> addBlock(new SobelBlock(parent)));
//        Button sepiaBlock = new MenuButton("Sepia", bm -> addBlock(new SepiaBlock(parent)));
//        Button rotateBlock = new MenuButton("Rotate", bm -> addBlock(new RotateBlock(parent)));
//        Button brightnessBlock = new MenuButton("Brihtness", bm -> addBlock(new BrightnessBlock(parent)));
//        Button gammaBlock = new MenuButton("Gamma", bm -> addBlock(new GammaBlock(parent)));
//        Button contrastBlock = new MenuButton("Contrast", bm -> addBlock(new ContrastBlock(parent)));
//        Button hueSaturationBlock = new MenuButton("HSB", bm -> addBlock(new HueSaturationValueBlock(parent)));

        utilSpace.getChildren().addAll(
                closeButton, outputBlock, constantBlock, mathBlock, node
//                , imageBlock, convolutionBlock, imagePreviewBlock,
//                invertBlock, greyscaleBlock, sobelBlock, sepiaBlock, rotateBlock, brightnessBlock, gammaBlock,
//                contrastBlock, hueSaturationBlock
        );

        for (Node button : utilSpace.getChildren()) {
            ((Region) button).setMaxWidth(Double.MAX_VALUE);
        }

        // opening animation of this menu, during which it can't be accidentally used
        this.setMouseTransparent(true);
        this.setScaleX(0.3);
        this.setScaleY(0.1);
        ScaleTransition opening = new ScaleTransition(byMouse ? Duration.ONE : Duration.millis(300), this);
        opening.setToX(1);
        opening.setToY(1);
        opening.setOnFinished(e -> this.setMouseTransparent(false));
        opening.play();
    }

    private void addBlock(Block block) {
        parent.addBlock(block);
        Bounds menuBounds = this.getBoundsInParent();
        int offSetY = (this.blockCounter % 5) * 20 + (block.getAllOutputs().isEmpty() ? 250 : 125);
        if (this.localToScene(Point2D.ZERO).getX() < 200) {
            // too close to the left side of screen, put block on the right
            int offSetX = (this.blockCounter % 5) * 10 + (block.isBottomMost() ? 50 : 100);
            block.relocate(menuBounds.getMaxX() + offSetX, menuBounds.getMinY() + offSetY);
        } else {
            int offSetX = (this.blockCounter % 5) * 10 - (block.isBottomMost() ? 400 : 200);
            block.relocate(menuBounds.getMinX() + offSetX, menuBounds.getMinY() + offSetY);
        }

        if (!block.isBottomMost()) {
            block.refreshContainer();
        }

        block.update();
        this.blockCounter++;
    }

    private void addDraggedBlock(TouchPoint touchPoint, Block block) {
        Point2D pos = parent.sceneToLocal(touchPoint.getSceneX(), touchPoint.getSceneY());
        parent.addBlock(block);
        block.relocate(pos.getX(), pos.getY());
        touchPoint.grab(block);
        block.sendUpdateDownStream();
    }

    /**
     * Closes this menu by removing it from it's parent.
     */
    public void close(boolean byMouse) {
        // disable it first, before removal in a closing animation
        this.setMouseTransparent(true);
        ScaleTransition closing = new ScaleTransition(byMouse ? Duration.ONE : Duration.millis(300), this);
        closing.setToX(0.3);
        closing.setToY(0.1);
        closing.setOnFinished(e -> parent.removeMenu(this));
        closing.play();
    }
}
