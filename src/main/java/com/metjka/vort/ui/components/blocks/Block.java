package com.metjka.vort.ui.components.blocks;

import com.google.common.collect.ImmutableMap;
import com.metjka.vort.ui.*;
import com.metjka.vort.ui.components.connections.ConnectionAnchor;
import com.metjka.vort.ui.components.connections.InputAnchor;
import com.metjka.vort.ui.components.connections.OutputAnchor;
import com.metjka.vort.ui.serialize.Bundleable;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Base block shaped UI Component that other visual elements will extend from.
 * Blocks can add input and output values by implementing the InputBlock and
 * OutputBlock interfaces.
 * <p>
 * MouseEvents are used for setting the selection state of a block, single
 * clicks can toggle the state to selected. When a block has already been
 * selected and receives another single left click it will toggle a contextual
 * menu for the block.
 * </p>
 * <p>
 * Each block implementation should also feature it's own FXML implementation.
 * </p>
 */
public abstract class Block extends StackPane implements Bundleable, ComponentLoader {
    private static final String BLOCK_ID_PARAMETER = "id";
    private static final String BLOCK_X_PARAMETER = "x";
    private static final String BLOCK_Y_PARAMETER = "y";
    private static final String BLOCK_PROPERTIES_PARAMETER = "properties";

    /**
     * The pane that is used to hold state and place all components on.
     */
    private final ToplevelPane toplevel;

    /**
     * The context that deals with dragging and touch event for this Block
     */
    protected DragContext dragContext;

    /**
     * Whether the anchor types are fresh
     */
    private boolean freshAnchorTypes;

    /**
     * Status of change updating process in this block.
     */
    private boolean updateInProgress;

    /**
     * The container to which this Block currently belongs
     */
    protected BlockContainer container;

    /**
     * Whether this block has a meaningful interpretation the current container context.
     */
    protected boolean inValidContext;

    /**
     * In order to serialize using simple class names we need some way to map the simple class
     * name to the full class names. This is one that should survive automatic refactoring of classes
     * into different packages - but won't survive class renaming
     */
    private static final Map<String, String> blockClassMap;

    static {
        Map<String, String> aMap = new HashMap<>();
        blockClassMap = Collections.unmodifiableMap(aMap);
    }

    public Block(ToplevelPane pane) {
        this.toplevel = pane;
        this.freshAnchorTypes = false;
        this.updateInProgress = false;
        this.container = pane;
        this.container.attachBlock(this);
        this.inValidContext = true;

        // only the actual shape should be selected for events, not the larger outside bounds
        this.setPickOnBounds(false);

        if (!this.belongsOnBottom()) {
            // make all non container blocks resize themselves around horizontal midpoint to reduce visual movement 
            this.translateXProperty().bind(this.widthProperty().divide(2).negate());
        }

        this.dragContext = new DragContext(this);
//        this.dragContext.setSecondaryClickAction((p, byMouse) -> CircleMenu.showFor(this, this.localToParent(p), byMouse));
        this.dragContext.setDragFinishAction(event -> refreshContainer());
        this.dragContext.setContactAction(x -> this.getStyleClass().add("activated"));
        this.dragContext.setReleaseAction(x -> this.getStyleClass().removeAll("activated"));
    }

    /**
     * @return the parent CustomUIPane of this component.
     */
    public final ToplevelPane getToplevel() {
        return this.toplevel;
    }

    public boolean isActivated() {
        return this.dragContext.isActivated();
    }

    /**
     * @return All InputAnchors of the block.
     */
    public abstract List<InputAnchor> getAllInputs();

    /**
     * @return All OutputAnchors of the Block.
     */
    public abstract List<OutputAnchor> getAllOutputs();

    public List<ConnectionAnchor> getAllAnchors() {
        List<ConnectionAnchor> result = new ArrayList<>(this.getAllInputs());
        result.addAll(this.getAllOutputs());
        return result;
    }

    /**
     * @return true if no connected output anchor exist
     */
    public boolean isBottomMost() {
        for (OutputAnchor anchor : getAllOutputs()) {
            if (anchor.hasConnection()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Starts a new (2 phase) change propagation process from this block.
     */
    public final void initiateConnectionChanges() {
        this.handleConnectionChanges(false);
        this.handleConnectionChanges(true);
    }

    /**
     * Connection change preparation; set fresh types in all anchors.
     */
    public final void prepareConnectionChanges() {
        if (this.updateInProgress || this.freshAnchorTypes) {
            return; // refresh anchor types in each block only once
        }
        this.freshAnchorTypes = true;
        this.getValue();

        this.inValidContext = this.checkValidInCurrentContainer();
        if (this.inValidContext) {
            this.getStyleClass().removeAll("invalid");
        } else {
            this.getStyleClass().removeAll("invalid");
            this.getStyleClass().add("invalid");
        }
    }

    /**
     * Set fresh types in all anchors of this block for the next typechecking cycle.
     */
    protected abstract Integer getValue();

    /**
     * Handle the expression and types changes caused by modified connections or values.
     * Propagate the changes through connected blocks, and if final phase trigger a visual update.
     *
     * @param finalPhase whether the change propagation is in the second (final) phase.
     */
    public void handleConnectionChanges(boolean finalPhase) {
        if (this.updateInProgress != finalPhase) {
            return; // avoid doing extra work and infinite recursion
        }

        if (!finalPhase) {
            // in first phase ensure that anchor types are refreshed
            this.prepareConnectionChanges();
        }

        this.updateInProgress = !finalPhase;
        this.freshAnchorTypes = false;

        // First make sure that all connected inputs will be updated too.        
        for (InputAnchor input : this.getAllInputs()) {
            input.getConnection().ifPresent(c -> c.handleConnectionChangesUpwards(finalPhase));
        }

        // propagate changes down from the output anchor to connected inputs
        this.getAllOutputs().forEach(output -> output.getOppositeAnchors().forEach(input -> input.handleConnectionChanges(finalPhase)));

        if (finalPhase) {
            // Now that the expressions and types are fully updated, initiate a visual refresh.
            Platform.runLater(this::invalidateVisualState);
        }
    }

    /**
     * Called when the VisualState changed.
     */
    public abstract void invalidateVisualState();

    /**
     * @return whether this block is visually shown below common blocks (is constant per instance).
     */
    public boolean belongsOnBottom() {
        return false;
    }

    /**
     * @return class-specific properties of this Block.
     */
    protected Map<String, Object> toBundleFragment() {
        return ImmutableMap.of();
    }

    /**
     * @return the container to which this block belongs
     */
    public BlockContainer getContainer() {
        return container;
    }

    /**
     * @return an independent copy of this Block, or Optional.empty if the internal state is too complex too copy.
     */
    public abstract Optional<Block> getNewCopy();

    /**
     * Remove all associations of this block with others in preparation of removal, including all connections
     */
    public void deleteAllLinks() {
        this.getAllInputs().forEach(InputAnchor::removeConnections);
        this.getAllOutputs().forEach(OutputAnchor::removeConnections);
        this.container.detachBlock(this);
        this.container = TrashContainer.instance;
    }

    /**
     * @return the bounds of this block in scene coordinates, excluding the parts sticking out such as anchors.
     */
    public Bounds getBodyBounds() {
        Node body = this.getChildren().get(0);
        return body.localToScene(body.getLayoutBounds());
    }

    /**
     * Scans for and attaches to a new container, if any
     */
    public void refreshContainer() {
        Bounds myBounds = this.getBodyBounds();

        Bounds fitBounds = this.localToParent(this.sceneToLocal(myBounds));

        toplevel.expandToFit(new BoundingBox(fitBounds.getMinX() - 10, fitBounds.getMinY() - 10, fitBounds.getWidth() + 20, fitBounds.getHeight() + 20));
    }

    /**
     * @return whether this block has a meaningful interpretation the current container.
     */
    public boolean checkValidInCurrentContainer() {
        return !(this.container instanceof TrashContainer);
    }

    @Override
    public Map<String, Object> toBundle() {
        return ImmutableMap.of(
                Bundleable.KIND, getClass().getSimpleName(),
                BLOCK_ID_PARAMETER, hashCode(),
                BLOCK_X_PARAMETER, getLayoutX(),
                BLOCK_Y_PARAMETER, getLayoutY(),
                BLOCK_PROPERTIES_PARAMETER, toBundleFragment()
        );
    }

    public static Block fromBundle(Map<String, Object> blockBundle,
                                   ToplevelPane toplevelPane,
                                   Map<Integer, Block> blockLookupTable)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String kind = (String) blockBundle.get(Bundleable.KIND);
        String className = blockClassMap.get(kind);
        Class<?> clazz = Class.forName(className);

        // Find the static "fromBundleFragment" method for the named type and call it
        Method fromBundleMethod = clazz.getDeclaredMethod("fromBundleFragment", ToplevelPane.class, Map.class);
        Block block = (Block) fromBundleMethod.invoke(null, toplevelPane, blockBundle.get(BLOCK_PROPERTIES_PARAMETER));
        block.setLayoutX((Double) blockBundle.get(BLOCK_X_PARAMETER));
        block.setLayoutY((Double) blockBundle.get(BLOCK_Y_PARAMETER));
        blockLookupTable.put(((Double) blockBundle.get(Block.BLOCK_ID_PARAMETER)).intValue(), block);

        // Ensure initialization of types related to the block
        block.initiateConnectionChanges();
        return block;
    }
}
