package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.ComponentLoader
import io.metjka.vortex.ui.Type
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.shape.Circle
import java.util.*

class InputDot<T>(block: Block, image: Type) : ConnectionDot<T>(block), ComponentLoader {

    @FXML
    var circle: Circle? = null

    init {
        loadFXML("InputDot")

    }

    override fun hasConnection(): Boolean {
        return connection.isPresent
    }

    override fun getAttachmentPoint(): Point2D {
        return topLevelPane.sceneToLocal(localToScene(Point2D(0.0, 0.0)))
    }

    override fun setNearbyWireReaction(goodness: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContainer(): BlockContainer {
        return block.getContainer()
    }

    override fun removeConnections() {
        if (connection.isPresent) {
            val con = connection.get()
            connection = Optional.empty()
            con.remove()
        }
    }

    fun onUpdate() {
        block.update()
    }

}