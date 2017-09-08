package io.metjka.vortex.ui

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.geometry.Bounds
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

internal class TouchDisplay(x: Double, y: Double, bounds: Bounds, private val touchId: Int) : Pane() {

    private val labelText: StringProperty
    private val circle: Circle
    private val label: Label

    init {
        labelText = SimpleStringProperty(createLabelText(x, y, touchId))

        circle = Circle(touchCircleRadius, touchCircleRadius, touchCircleRadius)
        circle.fill = Color.TRANSPARENT
        circle.stroke = COLOR_SEMI_TRANSPARENT
        children.add(circle)

        label = Label()
        label.textProperty().bindBidirectional(labelText)
        label.textFill = COLOR_OPAQUE
        label.relocate(0.0, touchCircleRadius * 2)
        children.add(label)

        background = Background(BackgroundFill(Color.TRANSPARENT, null, null))
    }

    /**
     * Move this TouchDisplay to a new x-y position.
     */
    fun moveTouchPoint(x: Double, y: Double) {
        labelText.set(createLabelText(x, y, touchId))
        val cb = circle.boundsInParent
        this.relocate(x - cb.minX - cb.width / 2, y - cb.minY - cb.height / 2)
    }

    private fun createLabelText(x: Double, y: Double, touchId: Int): String {
        return String.format("ID=%d, x=%f, y=%f", touchId, x, y)
    }

    companion object {

        val COLOR_OPAQUE = Color(1.0, 0.0, 0.0, 1.0)
        val COLOR_SEMI_TRANSPARENT = Color(1.0, 0.0, 0.0, 0.5)
        val touchCircleRadius = 50.0
    }
}
