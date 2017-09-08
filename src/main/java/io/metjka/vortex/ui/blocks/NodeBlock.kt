package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.connections.ConnectionDot
import io.metjka.vortex.ui.connections.InputDot
import io.metjka.vortex.ui.connections.OutputDot
import io.metjka.vortex.ui.loadXML
import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
import javafx.scene.layout.StackPane
import java.util.stream.Collectors

abstract class NodeBlock(val topLevelPane: TopLevelPane, blockName: String?) : StackPane() {

    val dragContext: DragContext

    init {
        loadXML(blockName!!)

        topLevelPane.attachBlock(this)
        isPickOnBounds = false

        dragContext = DragContext(this)
        dragContext.setDragFinishAction { refreshContainer() }
        dragContext.setContactAction { styleClass.add("activated") }
        dragContext.setReleaseAction { styleClass.removeAll("activated") }
    }

    abstract fun getAllInputs(): List<InputDot<*>>
    abstract fun getAllOutputs(): List<OutputDot<*>>
    abstract fun getNewCopy(): NodeBlock
    abstract fun update()

    fun getBodyBounds(): Bounds? {
        val node = children.get(0)
        return node.localToScene(node.layoutBounds)
    }

    fun getAllConnectionDots(): List<ConnectionDot> {
        val mutableListOf = mutableListOf<ConnectionDot>()
        mutableListOf.addAll(getAllInputs())
        mutableListOf.addAll(getAllOutputs())
        return mutableListOf
    }

    fun refreshContainer() {
        val bounds = getBodyBounds()
        val localToParent = localToParent(sceneToLocal(bounds))
        topLevelPane.expandToFit(BoundingBox(
                localToParent.minX - 10,
                localToParent.minY - 10,
                localToParent.width + 20,
                localToParent.height + 20
        ))
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
            return@flatMap it.getOppositeConnectionDots().stream()
        }.collect(Collectors.toList())
                .stream()
                .distinct()
                .forEach { it -> it.onUpdate() }
    }

    fun deleteAllLinks() {
        getAllConnectionDots().forEach { it.removeConnections() }
    }

}