package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.TopLevelPane
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.StrokeLineCap
import javafx.scene.transform.Transform
import mu.KotlinLogging

class ConnectionDrawer(val topLevelPane: TopLevelPane) : CubicCurve(), ChangeListener<Transform> {

    val log = KotlinLogging.logger { }

    private lateinit var dot: ConnectionDot<*>;

    constructor(outputDot: OutputDot<*>, topLevelPane: TopLevelPane) : this(topLevelPane) {
        dot = outputDot
        isMouseTransparent = true

        stroke = Color.FORESTGREEN
        strokeWidth = 4.0
        strokeLineCap = StrokeLineCap.ROUND
        fill = Color.TRANSPARENT

        topLevelPane.addWire(this)
        setStartPosition(dot.getAttachmentPoint())
        setFreePosition(dot.getAttachmentPoint())

        updateBezierControlPoints()
    }

    constructor(inputDot: InputDot<*>, topLevelPane: TopLevelPane) : this(topLevelPane) {
        dot = inputDot
        isMouseTransparent = true

        stroke = Color.FORESTGREEN
        strokeWidth = 4.0
        strokeLineCap = StrokeLineCap.ROUND
        fill = Color.TRANSPARENT

        topLevelPane.addWire(this)
        setStartPosition(dot.getAttachmentPoint())
        setFreePosition(dot.getAttachmentPoint())

        updateBezierControlPoints()

    }

    override fun changed(observable: ObservableValue<out Transform>?, oldValue: Transform?, newValue: Transform?) {

    }

    fun setFreePosition(point2D: Point2D) {
        when (dot) {
            is OutputDot -> {
                endX = point2D.x
                endY = point2D.y
            }
            is InputDot -> {
                startX = point2D.x
                startY = point2D.y
            }
            else -> throw IllegalArgumentException("Unknown dot type!")
        }
        updateBezierControlPoints()
    }

    fun setStartPosition(point: Point2D) {
        when (dot) {
            is OutputDot -> {
                startX = point.x
                startY = point.y
            }
            is InputDot -> {
                endX = point.x
                endY = point.y
            }
            else -> throw IllegalArgumentException("Unknown dot type!")
        }
        updateBezierControlPoints()
    }

    fun handleMouseDrag(event: MouseEvent?) {
        if (event != null) {
            val point2D = topLevelPane.sceneToLocal(event.sceneX, event.sceneY)
            this.setFreePosition(point2D)
        }
    }

    fun remove() {
        topLevelPane.removeWire(this)
    }

}