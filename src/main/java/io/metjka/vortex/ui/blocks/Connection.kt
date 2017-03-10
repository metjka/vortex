package io.metjka.vortex.ui.blocks

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
import java.util.*

class Connection<T> private constructor(val topLevelPane: TopLevelPane) : CubicCurve(), ChangeListener<Transform> {

    val log = KotlinLogging.logger { }

    val BEZIER_CONTROL_OFFSET = 150.0

    var startDot: OutputDot<T>? = null
    var endDot: InputDot<T>? = null

    init {
        topLevelPane.addWire(this)
        stroke = Color.FORESTGREEN
        strokeWidth = 4.0
        strokeLineCap = StrokeLineCap.ROUND
        fill = Color.TRANSPARENT
//        invalidateAnchorPositions()
    }

    constructor(start: OutputDot<T>, topLevelPane: TopLevelPane) : this(topLevelPane) {
        startDot = start
        endDot?.connection = Optional.of(this)
        startDot?.localToSceneTransformProperty()?.addListener(this)

        setStartPosition(startDot?.getAttachmentPoint())
        setEndPosition(startDot?.getAttachmentPoint())
    }

    constructor(end: InputDot<T>, topLevelPane: TopLevelPane) : this(topLevelPane) {
        endDot = end
        endDot?.connection = Optional.of(this)
        endDot?.localToSceneTransformProperty()?.addListener(this)

        setStartPosition(endDot?.getAttachmentPoint())
        setEndPosition(endDot?.getAttachmentPoint())
    }

    fun onConnectionCreated() {

    }

    fun remove() {
        startDot?.localToSceneTransformProperty()?.removeListener(this)
        endDot?.localToSceneTransformProperty()?.removeListener(this)

        startDot?.dropConnection(this)
        endDot?.removeConnections()
        topLevelPane.removeWire(this)

        endDot?.onUpdate()
    }

    override fun changed(observable: ObservableValue<out Transform>?, oldValue: Transform?, newValue: Transform?) {
        invalidateAnchorPositions()
    }

    fun setFreePosition(mouseEvent: MouseEvent) {
        val sceneToLocal = topLevelPane.sceneToLocal(mouseEvent.sceneX, mouseEvent.sceneY)
        val newX = sceneToLocal.x
        val newY = sceneToLocal.y
        val point2D = Point2D(newX, newY)
        when {
            endDot != null && startDot == null -> {
                startX = point2D.x
                startY = point2D.y

            }
            startDot != null && endDot == null -> {
                this.startX = point2D.x
                this.setStartY(point2D.y)
            }
            else -> {
                log.error("Wtf!")
            }
        }
        strokeDashArray.clear()
        updateBezierControlPoints(this)
    }

    fun invalidateAnchorPositions() {
        this.setStartPosition(this.startDot?.getAttachmentPoint())
        this.setEndPosition(this.endDot?.getAttachmentPoint())
    }

    private fun setStartPosition(point: Point2D?) {
        point?.let {
            this.startX = point.x
            this.startY = point.y
            updateBezierControlPoints(this)

        }
    }

    private fun setEndPosition(point: Point2D?) {
        point?.let {
            this.endX = point.x
            this.endY = point.y
            updateBezierControlPoints(this)
        }
    }

    protected fun updateBezierControlPoints(wire: CubicCurve) {
        val yOffset = getBezierYOffset(wire)
        wire.controlX1 = wire.startX + yOffset
        wire.controlY1 = wire.startY

        wire.controlX2 = wire.endX - yOffset
        wire.controlY2 = wire.endY
    }

    private fun getBezierYOffset(wire: CubicCurve): Double {
        val distX = Math.abs(wire.endX - wire.startX) / 3

        val diffY = wire.endY - wire.startY

        val distY = if (diffY > 0) {
            diffY / 2
        } else {
            Math.max(0.0, -diffY - 10)
        }
        if (distY < BEZIER_CONTROL_OFFSET) {
            if (distX < BEZIER_CONTROL_OFFSET) {
                // short lines are extra flexible
                return Math.max(1.0, Math.max(distX, distY))
            } else {
                return BEZIER_CONTROL_OFFSET
            }
        } else {
            return Math.cbrt(distY / BEZIER_CONTROL_OFFSET) * BEZIER_CONTROL_OFFSET
        }
    }
}