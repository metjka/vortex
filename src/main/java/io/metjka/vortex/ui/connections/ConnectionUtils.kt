package io.metjka.vortex.ui.connections

import javafx.scene.shape.CubicCurve

val BEZIER_CONTROL_OFFSET = 150.0

fun CubicCurve.updateBezierControlPoints() {
    val yOffset = getBezierYOffset(this)
    this.controlX1 = this.startX + yOffset
    this.controlY1 = this.startY

    this.controlX2 = this.endX - yOffset
    this.controlY2 = this.endY
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
