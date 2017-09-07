package io.metjka.vortex.ui.connections

import io.metjka.vortex.ui.TopLevelPane
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.StrokeLineCap
import javafx.scene.transform.Transform
import mu.KotlinLogging

class ConnectionDrawer private constructor(val topLevelPane: TopLevelPane) : CubicCurve(), ChangeListener<Transform> {

    val log = KotlinLogging.logger { }

    lateinit var dot: ConnectionDot;

    constructor(outputDot: OutputDot<*>, topLevelPane: TopLevelPane) : this(topLevelPane) {
        dot = outputDot
        apply {
            isMouseTransparent = true
            stroke = Color.BLANCHEDALMOND
            strokeWidth = 4.0
            strokeLineCap = StrokeLineCap.ROUND
            fill = Color.TRANSPARENT
        }

        topLevelPane.addWire(this)
        setStartPosition(dot.getAttachmentPoint())
        setFreePosition(dot.getAttachmentPoint())

        updateBezierControlPoints()
    }

    constructor(inputDot: InputDot<*>, topLevelPane: TopLevelPane) : this(topLevelPane) {
        dot = inputDot
        apply {
            isMouseTransparent = true
            stroke = Color.BLANCHEDALMOND
            strokeWidth = 4.0
            strokeLineCap = StrokeLineCap.ROUND
            fill = Color.TRANSPARENT
        }

        topLevelPane.addWire(this)
        setStartPosition(dot.getAttachmentPoint())
        setFreePosition(dot.getAttachmentPoint())

        updateBezierControlPoints()
    }

    override fun changed(observable: ObservableValue<out Transform>?, oldValue: Transform?, newValue: Transform?) {
        println("sika")
    }

    private fun setFreePosition(point2D: Point2D) {
        when (dot) {
            is OutputDot<*> -> {
                endX = point2D.x
                endY = point2D.y
            }
            is InputDot<*> -> {
                startX = point2D.x
                startY = point2D.y
            }
            else -> throw IllegalArgumentException("Unknown dot type!")
        }
        updateBezierControlPoints()
    }

    private fun setStartPosition(point: Point2D) {
        when (dot) {
            is OutputDot<*> -> {
                startX = point.x
                startY = point.y
            }
            is InputDot<*> -> {
                endX = point.x
                endY = point.y
            }
            else -> throw IllegalArgumentException("Unknown dot type!")
        }
        updateBezierControlPoints()
    }

    fun move(point2D: Point2D?) {
        if (point2D != null) {
            this.setFreePosition(point2D)
        }
    }

    fun handleRelease(mouseEvent: MouseEvent) {
        if (mouseEvent.isSynthesized) {
            remove()
        } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            handleReleaseOn(mouseEvent.getPickResult().getIntersectedNode())
        }
    }

    private fun handleReleaseOn(node: Node) {
        val findPickedAnchor = findPickedAnchor(node)
        if (findPickedAnchor == null) {
            remove()
        } else {
            println(findPickedAnchor)
            if (dot is OutputDot<*>) {

                val conn = buildConnection(findPickedAnchor)
                conn?.let {
                    topLevelPane.addConnection(conn)
                }
                remove()
            } else {
                val conn = buildConnection(findPickedAnchor)
                conn?.let {
                    topLevelPane.addConnection(conn)
                }
                remove()

            }
        }
    }

    private fun buildConnection(target: ConnectionDot): Connection? {
        var inp: InputDot<*>? = null
        var out: OutputDot<*>? = null
        when (dot) {
            is InputDot<*> -> {
                if (target is InputDot<*>) {
                    return null
                }
                inp = dot as InputDot<*>
                out = target as OutputDot<*>
            }
            is OutputDot<*> -> {
                if (target is OutputDot<*>) {
                    return null
                }
                inp = target as InputDot<*>
                out = dot as OutputDot<*>

                if (inp.hasConnection()) {
                    inp.removeConnections()
                }
            }
        }

        if (inp?.block == out?.block) {
            return null
        }

        return Connection(out, inp)
    }

    private fun findPickedAnchor(picked: Node): ConnectionDot? {
        var next: Node? = picked
        while (next != null) {
            if (next is Target) {
                return next.getAssociatedDot()
            }
            next = next.parent
        }

        return null
    }

    fun remove() {
        topLevelPane.removeWire(this)
    }

}