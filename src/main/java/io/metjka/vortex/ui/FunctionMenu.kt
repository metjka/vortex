package io.metjka.vortex.ui

import io.metjka.vortex.ui.blocks.*
import javafx.animation.ScaleTransition
import javafx.fxml.FXML
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.control.Accordion
import javafx.scene.control.Button
import javafx.scene.control.TitledPane
import javafx.scene.input.MouseEvent
import javafx.scene.input.TouchPoint
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.util.Duration

import java.util.ArrayList
import java.util.function.Consumer

class FunctionMenu(byMouse: Boolean, private val parent: TopLevelPane) : StackPane() {

    /**
     * The context that deals with dragging for this Menu
     */
    protected var dragContext: DragContext

    /**
     * Keep track of how many blocks are placed from this menu
     */
    private var blockCounter = 0

    private val categoryContainer = Accordion()
    @FXML
    lateinit var searchSpace: Pane

    @FXML
    lateinit var categorySpace: Pane
    @FXML
    lateinit var utilSpace: Pane

    init {
        this.dragContext = DragContext(this)
        loadXML("FunctionMenu")
        // drop all synthesized mouse events
        categoryContainer.addEventFilter(MouseEvent.ANY) { e -> if (e.isSynthesized) e.consume() }

        // Hiding all other categories on expanding one of them.
        val allCatPanes = ArrayList(categoryContainer.panes)
        categoryContainer.expandedPaneProperty().addListener { e ->
            val expPane = categoryContainer.expandedPane
            if (expPane != null) {
                categoryContainer.panes.retainAll(expPane)
            } else {
                categoryContainer.panes.clear()
                categoryContainer.panes.addAll(allCatPanes)
            }
        }

        this.categorySpace?.children?.add(categoryContainer)

        /* Create content for utilSpace. */
        val closeButton = MenuButton("Close", Consumer{ bm -> close(bm!!) })
        closeButton.styleClass.add("escape")

        val imageBlock = MenuButton("Preview", Consumer{ addBlock(NodeTestBlock(parent)) })
        val constantBlock = MenuButton("Value", Consumer{ addBlock(ValueBlock(parent)) })
        val mathBlock = MenuButton("Math", Consumer{ addBlock(MathBlock(parent)) })
        val resultBlock = MenuButton("Math result", Consumer{ addBlock(ResultBlock(parent)) })

        //        Button constantBlock = new MenuButton("Constant", bm -> addBlock(new ValueBlock(parent)));
        //        Button node = new MenuButton("Node", bm -> addBlock(new NodeTestBlock(parent)));
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

        utilSpace?.children?.addAll(
                closeButton, constantBlock, mathBlock, imageBlock,
                resultBlock)

        for (button in utilSpace?.children!!) {
            (button as Region).maxWidth = java.lang.Double.MAX_VALUE
        }

        // opening animation of this menu, during which it can't be accidentally used
        this.isMouseTransparent = true
        this.scaleX = 0.3
        this.scaleY = 0.1
        val opening = ScaleTransition(if (byMouse) Duration.ONE else Duration.millis(300.0), this)
        opening.toX = 1.0
        opening.toY = 1.0
        opening.setOnFinished { e -> this.isMouseTransparent = false }
        opening.play()
    }

    private fun addBlock(block: NodeBlock) {
        parent.addBlock(block)
        val menuBounds = this.boundsInParent
        val offSetY = this.blockCounter % 5 * 20 + if (block.getAllOutputs().isEmpty()) 250 else 125
        if (this.localToScene(Point2D.ZERO).x < 200) {
            // too close to the left side of screen, put block on the right
            val offSetX = this.blockCounter % 5 * 10 + if (block.isBottomMost()) 50 else 100
            block.relocate(menuBounds.maxX + offSetX, menuBounds.minY + offSetY)
        } else {
            val offSetX = this.blockCounter % 5 * 10 - if (block.isBottomMost()) 400 else 200
            block.relocate(menuBounds.minX + offSetX, menuBounds.minY + offSetY)
        }

        if (!block.isBottomMost()) {
            block.refreshContainer()
        }

        //        block.update();
        this.blockCounter++
    }

    private fun addDraggedBlock(touchPoint: TouchPoint, block: NodeBlock) {
        val pos = parent.sceneToLocal(touchPoint.sceneX, touchPoint.sceneY)
        parent.addBlock(block)
        block.relocate(pos.x, pos.y)
        touchPoint.grab(block)
        block.sendUpdateDownStream()
    }

    /**
     * Closes this menu by removing it from it's parent.
     */
    fun close(byMouse: Boolean) {
        // disable it first, before removal in a closing animation
        this.isMouseTransparent = true
        val closing = ScaleTransition(if (byMouse) Duration.ONE else Duration.millis(300.0), this)
        closing.toX = 0.3
        closing.toY = 0.1
        closing.setOnFinished { e -> parent.removeMenu(this) }
        closing.play()
    }
}
