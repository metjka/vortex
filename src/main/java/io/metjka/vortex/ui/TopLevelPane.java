package io.metjka.vortex.ui;

import com.google.common.collect.ImmutableMap;
import io.metjka.vortex.ui.blocks.Block;
import io.metjka.vortex.ui.connections.Connection;
import io.metjka.vortex.ui.connections.DrawWire;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The core Pane that represent the programming workspace.
 * It is a layered visualization of all blocks, wires, and menu elements.
 * And represents the toplevel container of all blocks.
 */
public class TopLevelPane extends Region implements BlockContainer, Bundleable {

    public static final String BLOCKS_SERIALIZED_NAME = "Blocks";
    public static final String CONNECTIONS_SERIALIZED_NAME = "Connections";

    /**
     * bottom pane layer for ordinary blocks
     */
    private final Pane blockLayer;

    /**
     * higher pane layer for connections wires
     */
    private final Pane wireLayer;

    /**
     * The set of blocks that logically belong to this top level
     */
    private final Set<Block> attachedBlocks;

    /**
     * Constructs a new instance.
     */
    public TopLevelPane() {
        super();
        this.attachedBlocks = new HashSet<>();

        this.blockLayer = new Pane();
        this.wireLayer = new Pane(this.blockLayer);
        this.getChildren().add(this.wireLayer);


        TouchContext context = new TouchContext(this, true);
        context.setPanningAction((deltaX, deltaY) -> {
            this.setTranslateX(this.getTranslateX() + deltaX);
            this.setTranslateY(this.getTranslateY() + deltaY);
        });
    }

    /**
     * Shows a new function menu at the specified location in this pane.
     */
    public void showFunctionMenuAt(double x, double y, boolean byMouse) {
        FunctionMenu menu = new FunctionMenu(byMouse, this);
        double verticalCenter = 150; // just a guesstimate, because computing it here is annoying
        Point2D scenePos = this.localToScene(x, y - verticalCenter);
        // avoid opening menu above top edge of the window
        Point2D newPos = this.sceneToLocal(scenePos.getX(), Math.max(0, scenePos.getY()));
        menu.relocate(newPos.getX(), newPos.getY());
        this.addMenu(menu);
    }

    /**
     * Remove the given block from this UI pane, including its connections.
     */
    public void removeBlock(Block block) {
        block.deleteAllLinks();
        this.blockLayer.getChildren().remove(block);
    }

    /**
     * Attempts to create a copy of a block and add it to this pane.
     */
    public void copyBlock(Block block) {
        block.getNewCopy().ifPresent(copy -> {
            this.addBlock(copy);
            copy.relocate(block.getLayoutX() + 20, block.getLayoutY() + 20);
            copy.update();
        });
    }

    public void addBlock(Block block) {
        this.blockLayer.getChildren().add(block);
    }

    public boolean addMenu(Pane menu) {
        return this.getChildren().add(menu);
    }

    public boolean removeMenu(Pane menu) {
        return this.getChildren().remove(menu);
    }

    public boolean addConnection(Connection connection) {
        return this.wireLayer.getChildren().add(connection);
    }

    public boolean removeConnection(Connection connection) {
        return this.wireLayer.getChildren().remove(connection);
    }

    public boolean addWire(DrawWire drawWire) {
        return this.getChildren().add(drawWire);
    }

    public boolean removeWire(DrawWire drawWire) {
        return this.getChildren().remove(drawWire);
    }

    public boolean addUpperTouchArea(Shape area) {
        return this.getChildren().add(area);
    }

    public boolean removeUpperTouchArea(Shape area) {
        return this.getChildren().remove(area);
    }

    public boolean addLowerTouchArea(Shape area) {
        return blockLayer.getChildren().add(area);
    }

    public boolean removeLowerTouchArea(Shape area) {
        return blockLayer.getChildren().remove(area);
    }


    public void clearChildren() {
        this.blockLayer.getChildren().remove(1, this.blockLayer.getChildren().size());
        this.wireLayer.getChildren().remove(1, this.wireLayer.getChildren().size());
        this.attachedBlocks.clear();
    }

    public Stream<Node> streamChildren() {
        Stream<Node> blocks = this.blockLayer.getChildren().stream().skip(1);
        Stream<Node> wires = this.wireLayer.getChildren().stream().skip(1);

        return Stream.concat(blocks, wires);
    }

    @Override
    public Map<String, Object> toBundle() {
        ImmutableMap.Builder<String, Object> bundle = ImmutableMap.builder();

        Stream<Node> blocks = this.blockLayer.getChildren().stream();

        bundle.put(BLOCKS_SERIALIZED_NAME, blocks
                .filter(n -> n instanceof Bundleable)
                .map(n -> ((Bundleable) n).toBundle())
                .toArray());

        bundle.put(CONNECTIONS_SERIALIZED_NAME, this.wireLayer.getChildren().stream()
                .filter(n -> n instanceof Bundleable)
                .map(n -> ((Bundleable) n).toBundle())
                .toArray());

        return bundle.build();
    }

    public void fromBundle(Map<String, Object> layers) {
        if (layers != null) {
            Map<Integer, Block> blockLookupTable = new HashMap<>();
            List<Map<String, Object>> blocksBundle = (ArrayList<Map<String, Object>>) layers.get(BLOCKS_SERIALIZED_NAME);
            if (blocksBundle != null) {
                for (Map<String, Object> bundle : blocksBundle) {
                    Block block;
                    try {
                        block = Block.fromBundle(bundle, this, blockLookupTable);
                        addBlock(block);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            List<Map<String, Object>> connectionsBundle = (ArrayList<Map<String, Object>>) layers.get(CONNECTIONS_SERIALIZED_NAME);
            if (connectionsBundle != null) {
                for (Map<String, Object> bundle : connectionsBundle) {
                    try {
                        Connection.fromBundle(bundle, blockLookupTable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public Bounds containmentBoundsInScene() {
        return this.localToScene(this.getBoundsInLocal());
    }

    /**
     * @param pos      the position to look around in coordinate system of this pane.
     * @param distance the maximum 'nearby' distance.
     */
    public List<ConnectionAnchor> allNearbyFreeAnchors(Point2D pos, double distance) {
        ArrayList<ConnectionAnchor> anchors = new ArrayList<>();
        Bounds testBounds = new BoundingBox(pos.getX() - distance, pos.getY() - distance, distance * 2, distance * 2);
        for (Block nearBlock : this.streamChildren().filter(n -> n instanceof Block).map(n -> (Block) n).filter(b -> b.getBoundsInParent().intersects(testBounds)).collect(Collectors.toList())) {
            for (ConnectionAnchor anchor : nearBlock.getAllAnchors()) {
                Point2D anchorPos = anchor.getAttachmentPoint();
                if (pos.distance(anchorPos) < distance && anchor.getWireInProgress() == null && !anchor.hasConnection()) {
                    anchors.add(anchor);
                }
            }
        }

        return anchors;
    }

    protected void cutIntersectingConnections(Shape cutter) {
        new ArrayList<>(this.wireLayer.getChildren()).stream()
                .filter(node -> node instanceof Connection).forEach(node -> {
            Connection wire = (Connection) node;
            if (((Path) Shape.intersect(wire, cutter)).getElements().size() > 0) {
                wire.remove();
            }
        });
    }

    @Override
    public void attachBlock(Block block) {
        this.attachedBlocks.add(block);
    }

    @Override
    public void detachBlock(Block block) {
        this.attachedBlocks.remove(block);
    }

    @Override
    public Stream<Block> getAttachedBlocks() {
        return this.attachedBlocks.stream();
    }

    @Override
    public BlockContainer getParentContainer() {
        return this;
    }

    @Override
    public Node asNode() {
        return this;
    }

    @Override
    public TopLevelPane getToplevel() {
        return this;
    }

    @Override
    public void expandToFit(Bounds bounds) {
        // The toplevel is large enough to fit practical everything
    }

    /**
     * Zooms the underlying main pane in/out with a ratio, up to reasonable limits.
     *
     * @param ratio the additional zoom factor to apply.
     */
    protected void zoom(double ratio) {
        double scale = getScaleX();

        /* Limit zoom to reasonable range. */
        if (scale <= 0.2 && ratio < 1) return;
        if (scale >= 3 && ratio > 1) return;

        setScaleX(scale * ratio);
        setScaleY(scale * ratio);
        setTranslateX(getTranslateX() * ratio);
        setTranslateY(getTranslateY() * ratio);
    }


}
