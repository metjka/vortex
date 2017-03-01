package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.ComponentLoader
import io.metjka.vortex.ui.serialize.Bundleable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Point2D
import javafx.scene.shape.CubicCurve
import javafx.scene.transform.Transform

/**
 * Created by Ihor Salnikov on 1.3.2017, 9:30 PM.
 * https://github.com/metjka/VORT
 */
class Connection(val start: OutputAnchor, val end: InputAnchor) : CubicCurve(), ChangeListener<Transform>, Bundleable, ComponentLoader {

    val BEZIER_CONTROL_OFFSET = 150.0

    private var error: Boolean

    init {
        this.isMouseTransparent = true
        this.fill = null

        this.error = false

        start.getPane().addConnection(this)
        this.invalidateAnchorPositions()
        this.start.addConnection(this)
        this.start.localToSceneTransformProperty().addListener(this)

        this.end.setConnection(this)
        this.end.localToSceneTransformProperty().addListener(this)
        this.end.handleConnectionChanges()

    }

    private fun invalidateAnchorPositions() {
        this.setStartPosition(this.start.getAttachmentPoint())
        this.setEndPosition(this.end.getAttachmentPoint())
    }

    fun remove() {
        this.start.localToSceneTransformProperty().removeListener(this)
        this.end.localToSceneTransformProperty().removeListener(this)

        this.start.dropConnection(this)
        this.end.removeConnections()
        this.start.getPane().removeConnection(this)

        this.start.handleConnectionChanges()
        this.end.handleConnectionChanges()
    }

    override fun changed(observable: ObservableValue<out Transform>?, oldValue: Transform?, newValue: Transform?) {
        invalidateAnchorPositions()
    }

    private fun setStartPosition(point: Point2D?) {
        point?.let {
            this.startX = point.getX()
            this.startY = point.getY()
            updateBezierControlPoints(this)

        }
    }

    private fun setEndPosition(point: Point2D?) {
        point?.let {
            this.endX = point.getX()
            this.endY = point.getY()
            updateBezierControlPoints(this)
        }
    }

    public fun invalidateVisualState() {
        TODO()
    }

    protected fun updateBezierControlPoints(wire: CubicCurve) {
        val yOffset = getBezierYOffset(wire)
        wire.controlX1 = wire.startX
        wire.controlY1 = wire.startY + yOffset
        wire.controlX2 = wire.endX
        wire.controlY2 = wire.endY - yOffset
    }

    private fun getBezierYOffset(wire: CubicCurve): Double {
        val distX = Math.abs(wire.endX - wire.startX) / 3
        val diffY = wire.endY - wire.startY
        val distY = if (diffY > 0) diffY / 2 else Math.max(0.0, -diffY - 10)
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

    fun hasError(): Boolean {
        return error
    }

    override fun toBundle(): MutableMap<String, Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}