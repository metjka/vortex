package io.metjka.vortex.ui

import io.metjka.vortex.ui.blocks.NodeBlock
import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
import javafx.scene.Node
import javafx.scene.layout.Region

import java.util.stream.Stream

/**
 * A dummy Block container for deleted blocks
 */
class TrashContainerNode private constructor() : NodeBlockContainer {

    override fun containmentBoundsInScene(): Bounds {
        return BoundingBox(0.0, 0.0, 0.0, 0.0)
    }

    override fun attachBlock(block: NodeBlock) {}

    override fun detachBlock(block: NodeBlock) {}

    override fun getAttachedBlocks(): Stream<NodeBlock> {
        return Stream.of<NodeBlock>()
    }

    override fun getParentContainer(): NodeBlockContainer {
        return this
    }

    override fun asNode(): Node {
        return Region()
    }

    override fun expandToFit(blockBounds: Bounds) {
        // everything fits in the trash
    }

    companion object {

        /**
         * The instance of the TrashContainerNode
         */
        var instance = TrashContainerNode()
    }

}
