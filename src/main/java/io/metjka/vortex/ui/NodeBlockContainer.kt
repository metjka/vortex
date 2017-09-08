package io.metjka.vortex.ui

import io.metjka.vortex.ui.blocks.NodeBlock
import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
import javafx.scene.Node

import java.util.stream.Stream

/**
 * A generic interface for block containers.
 */
interface NodeBlockContainer {

    /**
     * Gets the bounds to be used for testing what is inside this container, transformed into the coordinate space of its scene.
     */
    fun containmentBoundsInScene(): Bounds

    /**
     * Attach a block to this container
     */
    fun attachBlock(block: NodeBlock)

    /**
     * Detach a block from this container
     */
    fun detachBlock(block: NodeBlock)

    /**
     * @return a stream of all block attached to this container
     */
    fun getAttachedBlocks(): Stream<NodeBlock>

    /**
     * Check whether this container contains the specified block
     */
    fun containsBlock(block: NodeBlock): Boolean {
        return this.getAttachedBlocks().anyMatch { a -> block == a }
    }

    /**
     * @return the container to which this container belongs, maybe return itself if it is the outermost container
     */
    fun getParentContainer(): NodeBlockContainer

    /**
     * @return the TopLevelPane where this container is (indirectly) part of.
     * *
     * @throws IllegalStateException
     */
    fun getToplevel(): TopLevelPane {
        var cont = this
        while (cont.getParentContainer() !== cont) {
            cont = cont.getParentContainer()
        }

        if (cont is TopLevelPane) {
            return cont
        }

        throw IllegalStateException("Manipulating container that is not in a TopLevelPane")
    }

    fun asNode(): Node

    /**
     * @return Whether this container is (indirectly) contained with the other container.
     */
    fun isContainedWithin(other: NodeBlockContainer): Boolean {
        if (this === other) {
            return true
        }

        var target = this
        while (target.getParentContainer() !== target) {
            target = target.getParentContainer()
            if (target === other) {
                return true
            }
        }

        return false
    }

    /**
     * Grows the bounds of this container to fit the given additional bounds.

     * @param blockBounds of the Block that needs to fit in the container.
     */
    fun expandToFit(blockBounds: Bounds)

    companion object {

        /**
         * Return the union of two Bounds, i.e. a Bound that contains both.
         */
        fun union(a: Bounds, b: Bounds): Bounds {
            val left = Math.min(a.minX, b.minX)
            val right = Math.max(a.maxX, b.maxX)
            val top = Math.min(a.minY, b.minY)
            val bottom = Math.max(a.maxY, b.maxY)

            return BoundingBox(left, top, right - left, bottom - top)
        }
    }

}
