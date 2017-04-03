package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.TopLevelPane
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.shape.CubicCurve
import javafx.scene.transform.Transform
import mu.KotlinLogging

class ConnectionDrawer(topLevelPane: TopLevelPane) : CubicCurve(), ChangeListener<Transform> {

    val log = KotlinLogging.logger { }

    val BEZIER_CONTROL_OFFSET = 150.0


    constructor(outputDot: OutputDot<*>, topLevelPane: TopLevelPane) : this(topLevelPane) {

    }

    constructor(inputDot: InputDot<*>, topLevelPane: TopLevelPane) : this(topLevelPane){

    }

    override fun changed(observable: ObservableValue<out Transform>?, oldValue: Transform?, newValue: Transform?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}