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
import java.util.*

class Connection private constructor() : CubicCurve(), ChangeListener<Transform> {

    val log = KotlinLogging.logger { }


    var startDot: OutputDot<*>? = null
    var endDot: InputDot<*>? = null
    lateinit var topLevelPane: TopLevelPane

    init {
        stroke = Color.FORESTGREEN
        strokeWidth = 4.0
        strokeLineCap = StrokeLineCap.ROUND
        fill = Color.TRANSPARENT
//        invalidateAnchorPositions()
    }

    constructor(start: OutputDot<*>?, end: InputDot<*>?) : this() {
        topLevelPane = start?.topLevelPane!!

        startDot = start
        endDot = end

        endDot?.connection = Optional.of(this)
        startDot?.addConnection(this)

        startDot?.localToSceneTransformProperty()?.addListener(this)
        endDot?.localToSceneTransformProperty()?.addListener(this)

        setStartPosition(startDot?.getAttachmentPoint())
        setEndPosition(startDot?.getAttachmentPoint())

//        startDot?.onUpdate()
        invalidateAnchorPositions()
    }

    fun remove() {
        startDot?.localToSceneTransformProperty()?.removeListener(this)
        endDot?.localToSceneTransformProperty()?.removeListener(this)

        startDot?.dropConnection(this)
        endDot?.removeConnections()
        topLevelPane.removeConnection(this)

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
        this.updateBezierControlPoints()
    }

    fun invalidateAnchorPositions() {
        setStartPosition(this.startDot?.getAttachmentPoint())
        setEndPosition(this.endDot?.getAttachmentPoint())
    }

    private fun setStartPosition(point: Point2D?) {
        point?.let {
            startX = point.x
            startY = point.y
            updateBezierControlPoints()

        }
    }

    private fun setEndPosition(point: Point2D?) {
        point?.let {
            endX = point.x
            endY = point.y
            updateBezierControlPoints()
        }
    }

}
