package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.ComponentLoader
import io.metjka.vortex.ui.DragContext
import io.metjka.vortex.ui.ToplevelPane
import io.metjka.vortex.ui.connections.ConnectionAnchor
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import io.metjka.vortex.ui.serialize.Bundleable
import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
import javafx.scene.layout.StackPane
import java.util.*
import java.util.stream.Collectors.toList


abstract class Block(val toplevelPane: ToplevelPane, val blockName: String) : StackPane(), Bundleable, ComponentLoader {

    val dragContext: DragContext

    init {
        loadFXML(blockName)
        toplevelPane.attachBlock(this)
        isPickOnBounds = false

        dragContext = DragContext(this)
        dragContext.setDragFinishAction { refreshContainer() }
        dragContext.setContactAction { styleClass.add("activated") }
        dragContext.setReleaseAction { styleClass.removeAll("activated") }

    }

    fun isActivated(): Boolean {
        return dragContext.isActivated
    }

    abstract fun getAllInputs(): List<InputAnchor>
    abstract fun getAllOutputs(): List<OutputAnchor<*>>

    fun getAllAnchor(): List<ConnectionAnchor> {
        val mutableListOf = mutableListOf<ConnectionAnchor>()
        mutableListOf.addAll(getAllInputs())
        mutableListOf.addAll(getAllOutputs())
        return mutableListOf
    }

    fun isBottomMost(): Boolean {
        getAllOutputs().forEach {
            if (it.hasConnection()) {
                return false
            }
        }
        return true
    }

    fun sendUpdateDownStream() {
        getAllOutputs().stream().flatMap {
            return@flatMap it.getOppositeAnchors().stream()
        }.collect(toList())
                .stream()
                .distinct()
                .forEach { it.handleChange() }
    }

    abstract fun update()

    abstract fun getNewCopy(): Optional<Block>

    fun deleteAllLinks() {
        getAllAnchor().forEach { it.removeConnections() }
    }

    fun getBodyBounds(): Bounds? {
        val node = children.get(0)
        return node.localToScene(node.layoutBounds)
    }

    fun refreshContainer() {
        val bounds = getBodyBounds()
        val localToParent = localToParent(sceneToLocal(bounds))
        toplevelPane.expandToFit(BoundingBox(
                localToParent.minX - 10,
                localToParent.minY - 10,
                localToParent.width + 20,
                localToParent.height + 20
        ))
    }

    fun getContainer(): BlockContainer {
        return toplevelPane
    }

    override fun toBundle(): MutableMap<String, Any> {
        TODO()
    }

    fun fromBundle() {
        TODO()
    }

}