package io.metjka.vortex.ui

import com.google.common.collect.ImmutableMap
import io.metjka.vortex.ui.blocks.NodeBlock
import io.metjka.vortex.ui.connections.Connection
import io.metjka.vortex.ui.connections.ConnectionDot
import io.metjka.vortex.ui.serialize.Bundleable
import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.Path
import javafx.scene.shape.Shape
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * The core Pane that represent the programming workspace.
 * It is a layered visualization of all blocks, wires, and menu elements.
 * And represents the toplevel container of all blocks.
 */
class TopLevelPane() : Region(), NodeBlockContainer, Bundleable {
    /**
     * bottom pane layer for ordinary blocks
     */
    private val blockLayer: Pane

    /**
     * higher pane layer for connectionDeps wires
     */
    private val wireLayer: Pane

    /**
     * The set of blocks that logically belong to this top level
     */
    private val attachedBlocks: MutableSet<NodeBlock>

    /**
     * Constructs a new instance.
     */
    init {
        this.attachedBlocks = HashSet<NodeBlock>()

        this.wireLayer = Pane()
        this.blockLayer = Pane(wireLayer)
        this.children.add(this.blockLayer)


        val context = TouchContext(this, true)
        context.setPanningAction { deltaX, deltaY ->
            this.translateX = this.translateX + deltaX!!
            this.translateY = this.translateY + deltaY!!
        }
    }

    /**
     * Shows a new function menu at the specified location in this pane.
     */
    fun showFunctionMenuAt(x: Double, y: Double, byMouse: Boolean) {
        val menu = FunctionMenu(byMouse, this)
        val verticalCenter = 150.0 // just a guesstimate, because computing it here is annoying
        val scenePos = this.localToScene(x, y - verticalCenter)
        // avoid opening menu above top edge of the window
        val newPos = this.sceneToLocal(scenePos.x, Math.max(0.0, scenePos.y))
        menu.relocate(newPos.x, newPos.y)
        this.addMenu(menu)
    }

    /**
     * Remove the given block from this UI pane, including its connectionDeps.
     */
    fun removeBlock(block: NodeBlock) {
        block.deleteAllLinks()
        this.blockLayer.children.remove(block)
    }

    /**
     * Attempts to create a copy of a block and add it to this pane.
     */
    fun copyBlock(block: NodeBlock) {
        block.getNewCopy().let { copy ->
            this.addBlock(copy)
            copy.relocate(block.layoutX + 20, block.layoutY + 20)
            copy.update()
        }
    }

    fun addBlock(block: NodeBlock) {
        this.blockLayer.children.add(block)
    }

    fun addMenu(menu: Pane): Boolean {
        return this.children.add(menu)
    }

    fun removeMenu(menu: Pane): Boolean {
        return this.children.remove(menu)
    }

    fun addConnection(connectionDep: CubicCurve): Boolean {
        return this.wireLayer.children.add(connectionDep)
    }

    fun removeConnection(connectionDep: CubicCurve): Boolean {
        return this.wireLayer.children.remove(connectionDep)
    }

    fun addWire(drawWire: CubicCurve): Boolean {
        return this.children.add(drawWire)
    }

    fun removeWire(drawWire: CubicCurve): Boolean {
        return this.children.remove(drawWire)
    }

    fun addUpperTouchArea(area: Shape): Boolean {
        return this.children.add(area)
    }

    fun removeUpperTouchArea(area: Shape): Boolean {
        return this.children.remove(area)
    }

    fun addLowerTouchArea(area: Shape): Boolean {
        return blockLayer.children.add(area)
    }

    fun removeLowerTouchArea(area: Shape): Boolean {
        return blockLayer.children.remove(area)
    }


    fun clearChildren() {
        this.blockLayer.children.remove(1, this.blockLayer.children.size)
        this.wireLayer.children.remove(1, this.wireLayer.children.size)
        this.attachedBlocks.clear()
    }

    fun streamChildren(): Stream<Node> {
        val blocks = this.blockLayer.children.stream().skip(1)
        val wires = this.wireLayer.children.stream().skip(1)

        return Stream.concat(blocks, wires)
    }

    override fun toBundle(): Map<String, Any> {
        val bundle = ImmutableMap.builder<String, Any>()

        val blocks = this.blockLayer.children.stream()

        bundle.put(BLOCKS_SERIALIZED_NAME, blocks
                .filter { n -> n is Bundleable }
                .map { n -> (n as Bundleable).toBundle() }
                .toArray())

        bundle.put(CONNECTIONS_SERIALIZED_NAME, this.wireLayer.children.stream()
                .filter { n -> n is Bundleable }
                .map { n -> (n as Bundleable).toBundle() }
                .toArray())

        return bundle.build()
    }

    //    public void fromBundle(Map<String, Object> layers) {
    //        if (layers != null) {
    //            Map<Integer, Block> blockLookupTable = new HashMap<>();
    //            List<Map<String, Object>> blocksBundle = (ArrayList<Map<String, Object>>) layers.get(BLOCKS_SERIALIZED_NAME);
    //            if (blocksBundle != null) {
    //                for (Map<String, Object> bundle : blocksBundle) {
    //                    Block block;
    //                    try {
    //                        block = Block.fromBundle(bundle, this, blockLookupTable);
    //                        addBlock(block);
    //                    } catch (Exception e) {
    //                        e.printStackTrace();
    //                    }
    //                }
    //            }
    //
    //            List<Map<String, Object>> connectionsBundle = (ArrayList<Map<String, Object>>) layers.get(CONNECTIONS_SERIALIZED_NAME);
    //            if (connectionsBundle != null) {
    //                for (Map<String, Object> bundle : connectionsBundle) {
    //                    try {
    //                        ConnectionDep.fromBundle(bundle, blockLookupTable);
    //                    } catch (Exception e) {
    //                        e.printStackTrace();
    //                    }
    //                }
    //            }
    //        }
    //    }

    override fun containmentBoundsInScene(): Bounds {
        return this.localToScene(this.boundsInLocal)
    }

    /**
     * @param pos      the position to look around in coordinate system of this pane.
     * *
     * @param distance the maximum 'nearby' distance.
     */
    fun allNearbyFreeAnchors(pos: Point2D, distance: Double): List<ConnectionDot> {
        val anchors = ArrayList<ConnectionDot>()
        val testBounds = BoundingBox(pos.x - distance, pos.y - distance, distance * 2, distance * 2)
        for (nearBlock in this.streamChildren()
                .filter { n -> n is NodeBlock }
                .map { n -> n as NodeBlock }
                .filter { b -> b.boundsInParent.intersects(testBounds) }
                .collect(Collectors.toList<NodeBlock>())) {
            for (anchor in nearBlock.getAllConnectionDots()) {
                val anchorPos = anchor.getAttachmentPoint()
                if (pos.distance(anchorPos) < distance && anchor.wireInProgress == null && !anchor.hasConnection()) {
                    anchors.add(anchor)
                }
            }
        }

        return anchors
    }

    fun cutIntersectingConnections(cutter: Shape) {
        ArrayList(this.wireLayer.children).stream()
                .filter { node -> node is Connection }.forEach { node ->
            val wire = node as Connection
            if ((Shape.intersect(wire, cutter) as Path).elements.size > 0) {
                wire.remove()
            }
        }
    }

    override fun attachBlock(block: NodeBlock) {
        this.attachedBlocks.add(block)
    }

    override fun detachBlock(block: NodeBlock) {
        this.attachedBlocks.remove(block)
    }

    override fun getAttachedBlocks(): Stream<NodeBlock> {
        return this.attachedBlocks.stream()
    }

    override fun getParentContainer(): NodeBlockContainer = this

    override fun asNode(): Node {
        return this
    }

    override fun getToplevel(): TopLevelPane = this

    override fun expandToFit(bounds: Bounds) {
        // The toplevel is large enough to fit practical everything
    }

    /**
     * Zooms the underlying main pane in/out with a ratio, up to reasonable limits.

     * @param ratio the additional zoom factor to apply.
     */
    fun zoom(ratio: Double) {
        val scale = scaleX

        /* Limit zoom to reasonable range. */
        if (scale <= 0.2 && ratio < 1) return
        if (scale >= 3 && ratio > 1) return

        scaleX = scale * ratio
        scaleY = scale * ratio
        translateX = translateX * ratio
        translateY = translateY * ratio
    }

    companion object {

        val BLOCKS_SERIALIZED_NAME = "Blocks"
        val CONNECTIONS_SERIALIZED_NAME = "Connections"
    }


}
