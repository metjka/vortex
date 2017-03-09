package io.metjka.vortex.ui.blocks

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.shape.CubicCurve
import javafx.scene.transform.Transform
import mu.KotlinLogging
import java.util.*

class Connection<T> private constructor() : CubicCurve(), ChangeListener<Transform> {

    val log = KotlinLogging.logger { }

    val BEZIER_CONTROL_OFFSET = 150.0

    var startDot: OutputDot<T>? = null
    var endDot: InputDot<T>? = null

    init {
//        set
    }

    constructor(start: OutputDot<T>) : this() {
        start.connection = Optional.of(this)
        start.localToSceneTransformProperty().addListener(this)

    }

    constructor(end: InputDot<T>) : this()

    fun remove() {
        startDot?.localToSceneTransformProperty()?.removeListener(this)
        endDot?.localToSceneTransformProperty()?.removeListener(this)

        startDot?.dropConnection(this)
        endDot?.removeConnections()
//        startDot?.topLevelPane?.removeConnection(this)

        endDot?.onUpdate()
    }

    override fun changed(observable: ObservableValue<out Transform>?, oldValue: Transform?, newValue: Transform?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setFreePosition(mouseEvent: MouseEvent) {
        val scaleFactor = this.scaleX
        val newX = this.layoutX + mouseEvent.x * scaleFactor
        val newY = this.layoutY + mouseEvent.y * scaleFactor
        val point2D = Point2D(newX, newY)
        when {
            endDot != null && startDot == null -> {
                this.endX = point2D.x
                this.setEndY(point2D.y)
            }
            startDot != null && endDot == null -> {
                this.startX = point2D.x
                this.setStartY(point2D.y)
            }
            else -> {
                log.error("Wtf!")
            }
        }
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
        val yOffset = 150
        wire.controlX1 = wire.startX
        wire.controlY1 = wire.startY + yOffset
        wire.controlX2 = wire.endX
        wire.controlY2 = wire.endY - yOffset
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