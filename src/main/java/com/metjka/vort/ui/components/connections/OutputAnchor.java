package com.metjka.vort.ui.components.connections;

import com.google.common.collect.ImmutableMap;
import com.metjka.vort.ui.BlockContainer;
import com.metjka.vort.ui.Type;
import com.metjka.vort.ui.components.Target;
import com.metjka.vort.ui.components.blocks.Block;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.metjka.vort.ui.components.connections.DrawWire.NEUTRAL_TYPE_REACTION;
import static com.metjka.vort.ui.components.connections.DrawWire.GOOD_TYPE_REACTION;
import static com.metjka.vort.ui.components.connections.DrawWire.WRONG_TYPE_REACTION;

/**
 * Anchor that specifically functions as an output.
 */
public class OutputAnchor extends ConnectionAnchor implements Target {

    /**
     * The visual representation of the OutputAnchor.
     */
    @FXML
    private Shape visibleAnchor;

    /**
     * The invisible part of the OutputAnchor (the touch zone).
     */
    @FXML
    private Shape invisibleAnchor;

    /**
     * The thing sticking out of an unconnected OutputAnchor.
     */
    @FXML
    private Shape openWire;

    /**
     * The thing that is shown when this anchor is used as a boolean guard.
     */
    @FXML
    private Shape guardMarker;

    /**
     * The connections this anchor has, can be empty for no connections.
     */
    protected List<Connection> connections;
    private final Type type;
    private final int position;


    public int getPosition() {
        return position;
    }

    /**
     * @param block The block this Anchor is connected to
     */
    public OutputAnchor(Block block, int position, Type type) {
        super(block);
        this.type = type;
        this.loadFXML("OutputAnchor");
        this.connections = new ArrayList<>();
        this.position = position;
    }


    @Override
    public ConnectionAnchor getAssociatedAnchor() {
        return this;
    }

    /**
     * Get the input anchors on the other side of the Connection from this anchor.
     *
     * @return A list of each input anchor for each Connection this anchor has.
     */
    public List<InputAnchor> getOppositeAnchors() {
        List<InputAnchor> list = new ArrayList<>();
        for (Connection c : this.connections) {
            list.add(c.getEndAnchor());
        }
        return list;
    }

    @Override
    public boolean hasConnection() {
        return !this.connections.isEmpty();
    }

    /**
     * Adds the given connection to the connections this anchor has.
     *
     * @param connection The connection to add.
     */
    protected void addConnection(Connection connection) {
        this.connections.add(connection);
        this.openWire.setVisible(false);
        this.guardMarker.setVisible(false);
    }

    /**
     * Drops the connection from this anchor
     *
     * @param connection Connection to disconnect from.
     */
    protected void dropConnection(Connection connection) {
        if (this.connections.contains(connection)) {
            this.connections.remove(connection);
            this.openWire.setVisible(!this.hasConnection());
        }
    }

    @Override
    public void removeConnections() {
        while (!this.connections.isEmpty()) {
            Connection connection = this.connections.remove(0);
            connection.remove();
        }
        this.openWire.setVisible(true);
        if (this.getWireInProgress() != null) {
            this.getWireInProgress().remove();
        }
    }

    @Override
    public Point2D getAttachmentPoint() {
        return this.getPane().sceneToLocal(this.localToScene(new Point2D(0, 7)));
    }

    /**
     * Initiate connection changes at the Block this anchor is attached to.
     */
    public void initiateConnectionChanges() {
        this.block.handleConnectionChanges();
    }

    /**
     * Prepare connection changes in the block this anchor belongs to.
     */
    public void prepareConnectionChanges() {
        this.block.prepareConnectionChanges();
    }

    @Override
    protected void setNearbyWireReaction(int goodness) {

        switch (goodness) {
            case GOOD_TYPE_REACTION: {
                this.openWire.setStroke(Color.DARKGREEN);
                this.openWire.setStrokeWidth(5);
                this.visibleAnchor.setFill(Color.DARKGREEN);
                this.guardMarker.setStroke(Color.DARKGREEN);
                break;
            }
            case NEUTRAL_TYPE_REACTION: {
                this.openWire.setStroke(Color.DODGERBLUE);
                this.openWire.setStrokeWidth(5);
                this.visibleAnchor.setFill(Color.DODGERBLUE);
                this.guardMarker.setStroke(Color.DODGERBLUE);
                break;
            }
            case WRONG_TYPE_REACTION: {
                this.openWire.setStroke(Color.RED);
                this.openWire.setStrokeWidth(3);
                this.visibleAnchor.setFill(Color.RED);
                this.guardMarker.setStroke(Color.RED);
                break;
            }
            default: {
                this.openWire.setStroke(Color.BLACK);
                this.openWire.setStrokeWidth(3);
                this.visibleAnchor.setFill(Color.BLACK);
                this.guardMarker.setStroke(Color.BLACK);
                break;
            }
        }

    }

    @Override
    public void setWireInProgress(DrawWire wire) {
        super.setWireInProgress(wire);
        if (wire == null) {
            this.invalidateVisualState();
            this.invisibleAnchor.setMouseTransparent(false);
        } else {
            this.openWire.setVisible(false);
            this.guardMarker.setVisible(false);
            this.invisibleAnchor.setMouseTransparent(true);
        }
    }

    public void invalidateVisualState() {
        this.openWire.setVisible(!this.hasConnection());
        this.guardMarker.setVisible(false);
    }

    @Override
    public BlockContainer getContainer() {
        return this.block.getContainer();
    }

    @Override
    public Map<String, Object> toBundle() {
        ImmutableMap.Builder<String, Object> bundle = ImmutableMap.builder();
        Block block = this.block;
        bundle.put(BLOCK_LABEL, block.hashCode());
        return bundle.build();
    }

    public Type getType() {
        return type;
    }

    public void sendUpdateDownSteam() {
        block.sendUpdateDownSteam();
    }
}
