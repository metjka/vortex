package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.BlockContainer
import io.metjka.vortex.ui.blocks.Block
import javafx.geometry.Point2D

/**
 * Created by Ihor Salnikov on 1.3.2017, 10:46 PM.
 * https://github.com/metjka/VORT
 */
class InputAnchorK(block: Block): ConnectionAnchor(block), Target {

    init {
        loadFXML("InputAnchor")


    }


    override fun getAssociatedAnchor(): ConnectionAnchor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toBundle(): MutableMap<String, Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeConnections() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasConnection(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttachmentPoint(): Point2D {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNearbyWireReaction(goodness: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContainer(): BlockContainer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}