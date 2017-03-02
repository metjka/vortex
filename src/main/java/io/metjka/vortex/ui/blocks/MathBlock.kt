package io.metjka.vortex.ui.blocks

import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import java.util.*

/**
 * Created by Ihor Salnikov on 2.3.2017, 10:30 PM.
 * https://github.com/metjka/VORT
 */
class MathBlock(topLevelPane: TopLevelPane) : Block(topLevelPane, MathBlock::class.simpleName) {

    @FXML
    private val inputSpace1: Pane? = null

    @FXML
    private val inputSpace2: Pane? = null

    @FXML
    private val outputSpace: Pane? = null

    @FXML
    private val textField: TextField? = null

    @FXML
    private val methodComboBox: ComboBox<String>? = null

    enum class Method {
        ADD, EXT, MIX, MIN, MAX, MULL
    }

    var method: Method

    val inputAnchor1 = InputAnchor<Int>(this, Type.NUMBER)

    val inputAnchor2 = InputAnchor<Int>(this, Type.NUMBER)

    val outputAnchor = OutputAnchor<Int>(this, Type.NUMBER)

    init {
        inputSpace1?.children?.add(0, inputAnchor1)
        inputSpace2?.children?.add(0, inputAnchor2)

        outputSpace?.children?.add(0, outputAnchor)

        outputAnchor.property?.value = 0
        method = Method.ADD

        outputAnchor.property?.addListener { _ ->
            update()
        }

        methodComboBox?.getSelectionModel()
                ?.selectedItemProperty()?.addListener { _, _, newValue ->
            method = getMethod(newValue)
            update()
        }
    }

    private fun getMethod(methodString: String): Method {

        when (methodString) {
            "ADD" -> return Method.ADD
            "EXT" -> return Method.EXT
            "MIX" -> return Method.MIX
            "MIN" -> return Method.MIN
            "MAX" -> return Method.MAX
            "MULL" -> return Method.MULL
            else ->
                throw IllegalArgumentException("Wrong method name!")
        }
    }

    private fun calculate(a: Int, b: Int): Int {
        when (method) {
            MathBlock.Method.ADD -> return a + b
            MathBlock.Method.EXT -> return a - b
            MathBlock.Method.MIX -> return (a + b) / 2
            MathBlock.Method.MIN -> return Math.min(a, b)
            MathBlock.Method.MAX -> return Math.max(a, b)
            MathBlock.Method.MULL -> return a * b
        }
    }

    override fun getAllInputs(): List<InputAnchor<*>> {
        return listOf(inputAnchor1, inputAnchor2)
    }

    override fun getAllOutputs(): List<OutputAnchor<*>> {
        return listOf(outputAnchor)
    }

    override fun update() {
        getAllInputs().forEach { it.invalidateVisualState() }
        outputAnchor.invalidateVisualState()

        val value1: Int? = inputAnchor1.getOppositeAnchor().get().property?.get()
        val value2: Int? = inputAnchor2.getOppositeAnchor().get().property?.get()

        value1?.let {
            value2?.let {
                val result = calculate(value1, value2)
                outputAnchor.property?.value = result
            }
        }


    }

    override fun getNewCopy(): Optional<Block> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}