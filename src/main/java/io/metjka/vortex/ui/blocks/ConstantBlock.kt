package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputDot
import io.metjka.vortex.ui.connections.OutputDot

class ConstantBlock(topLevelPane: TopLevelPane) : NodeBlock(topLevelPane, ConstantBlock::class.simpleName) {

    val outputDot = OutputDot<Number>(this, Type.NUMBER)

    init {

    }

    override fun update() {

    }

    override fun getAllInputs(): List<InputDot<*>> {
        return emptyList()
    }

    override fun getAllOutputs(): List<OutputDot<*>> {
        return listOf(outputDot)
    }

    override fun getNewCopy(): NodeBlock {
        return this
    }

}