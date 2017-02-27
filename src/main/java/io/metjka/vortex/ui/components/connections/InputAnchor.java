package io.metjka.vortex.ui.components.connections;

import com.google.common.collect.ImmutableMap;
import io.metjka.vortex.ui.BlockContainer;
import io.metjka.vortex.ui.Type;
import io.metjka.vortex.ui.components.blocks.Block;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.Map;
import java.util.Optional;

/**
 * ConnectionAnchor that specifically functions as an input.
 */
public class InputAnchor extends ConnectionAnchor implements Target {

    /**
     * The visual representation of the InputAnchor.
     */
    @FXML
    protected Shape visibleAnchor;

    /**
     * The invisible part of the InputAnchor (the touch zone).
     */
    @FXML
    protected Shape invisibleAnchor;

    /**
     * The thing sticking out of an unconnected InputAnchor.
     */
    @FXML
    protected Shape openWire;

    /**
     * The Optional connection this anchor has.
     */
    private Optional<Connection> connection;

    /**
     * The local type of this anchor
     */
    private Type type;

    /**
     * Property storing the error state.
     */
    private BooleanProperty errorState;

    /**
     * @param block The Block this anchor is connected to.
     */
    public InputAnchor(Block block) {
        super(block);
        this.loadFXML("InputAnchor");

        this.connection = Optional.empty();

        this.errorState = new SimpleBooleanProperty(false);
        this.errorState.addListener(this::checkError);
    }

    /**
     * @param block The Block this anchor is connected to.
     * @param type  The type constraint for this anchor.
     */
    public InputAnchor(Block block, Type type) {
        this(block);
        this.type = type;
    }

    @Override
    public ConnectionAnchor getAssociatedAnchor() {
        return this;
    }

    /**
     * @param state The new error state for this ConnectionAnchor.
     */
    protected void setErrorState(boolean state) {
        this.errorState.set(state);
    }

    /**
     * @return The Optional connection this anchor has.
     */
    public Optional<Connection> getConnection() {
        return this.connection;
    }

    /**
     * Sets the connection of this anchor.
     *
     * @param connection The connection to set.
     */
    protected void setConnection(Connection connection) {
        this.connection = Optional.of(connection);
        this.openWire.setVisible(false);
    }

    @Override
    public void removeConnections() {
        if (this.connection.isPresent()) {
            Connection conn = this.connection.get();
            this.connection = Optional.empty();
            conn.remove();
        }
        this.setErrorState(false);
        this.openWire.setVisible(true);
        if (this.getWireInProgress() != null) {
            this.getWireInProgress().remove();
        }
    }

    @Override
    public boolean hasConnection() {
        return this.connection.isPresent();
    }

    /**
     * @return True if this anchor has an error free connection
     */
    public boolean hasValidConnection() {
        return this.connection.isPresent() && !(this.errorState.get() || this.connection.get().hasScopeError());
    }

    @Override
    public Point2D getAttachmentPoint() {
        return this.getPane().sceneToLocal(this.localToScene(new Point2D(0, -7)));
    }

    /**
     * @return the local type of this anchor
     */
    public Type getType() {
        return this.type;
    }

    /**
     * @return the string representation of the in- or output type.
     */
    public final String getStringType() {
        return type.toString();
    }

    public void setExactRequiredType(Type type) {
        this.type = type;
    }

    /**
     * @return Optional of the connection's opposite output anchor.
     */
    public Optional<OutputAnchor> getOppositeAnchor() {
        return this.connection.map(c -> c.getStartAnchor());
    }

    /**
     * ChangeListener that will set the error state if isConnected().
     */
    public void checkError(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        ObservableList<String> style = this.visibleAnchor.getStyleClass();
        style.removeAll("error");
        if (newValue) {
            style.add("error");
        }
    }

    @Override
    protected void setNearbyWireReaction(int goodness) {

        switch (goodness) {
            case DrawWire.GOOD_TYPE_REACTION: {
                this.openWire.setStroke(Color.DARKGREEN);
                this.openWire.setStrokeWidth(5);
                this.visibleAnchor.setFill(Color.DARKGREEN);
                break;
            }
            case DrawWire.NEUTRAL_TYPE_REACTION: {
                this.openWire.setStroke(Color.DODGERBLUE);
                this.openWire.setStrokeWidth(5);
                this.visibleAnchor.setFill(Color.DODGERBLUE);
                break;
            }
            case DrawWire.WRONG_TYPE_REACTION: {
                this.openWire.setStroke(Color.RED);
                this.openWire.setStrokeWidth(3);
                this.visibleAnchor.setFill(Color.RED);
                break;
            }
            default: {
                this.openWire.setStroke(Color.BLACK);
                this.openWire.setStrokeWidth(3);
                this.visibleAnchor.setFill(Color.BLACK);
                break;
            }
        }
    }

    @Override
    public void setWireInProgress(DrawWire wire) {
        super.setWireInProgress(wire);
        if (wire == null) {
            this.openWire.setVisible(!this.hasConnection());
            this.invisibleAnchor.setMouseTransparent(false);
        } else {
            this.openWire.setVisible(false);
            this.invisibleAnchor.setMouseTransparent(true);
        }
    }

    /**
     * Called when the VisualState changed.
     */
    public void invalidateVisualState() {
        this.connection.ifPresent(connection -> connection.invalidateVisualState());
    }

    @Override
    public BlockContainer getContainer() {
        return this.block.getContainer();
    }

    @Override
    public String toString() {
        return "InputAnchor for " + this.block;
    }

    @Override
    public Map<String, Object> toBundle() {
        ImmutableMap.Builder<String, Object> bundle = ImmutableMap.builder();
        bundle.put(BLOCK_LABEL, this.block.hashCode());
        bundle.put(ANCHOR_LABEL, block.getAllInputs().indexOf(this));
        return bundle.build();
    }

}
