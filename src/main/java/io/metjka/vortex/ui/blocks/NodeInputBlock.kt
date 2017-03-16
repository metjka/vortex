package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.connections.InputDot
import io.metjka.vortex.ui.connections.OutputDot


class NodeInputBlock(topLevelPane: TopLevelPane) : NodeBlock(topLevelPane, NodeInputBlock::class.simpleName) {


    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllInputs(): List<InputDot<*>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllOutputs(): List<OutputDot<*>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNewCopy(): NodeBlock {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

