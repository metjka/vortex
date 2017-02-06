package com.metjka.vort.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.precessing.FastImage
import com.metjka.vort.precessing.ImageRotation
import com.metjka.vort.ui.ToplevelPane
import com.metjka.vort.ui.Type
import com.metjka.vort.ui.components.connections.InputAnchor
import com.metjka.vort.ui.components.connections.OutputAnchor
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.layout.VBox
import mu.KotlinLogging
import rx.Single
import rx.schedulers.Schedulers
import java.util.*

class RotateBlock(val toplevelPane: ToplevelPane) : ValueBlock<FastImage>(toplevelPane, RotateBlock::class.simpleName) {

    val log = KotlinLogging.logger { }

    val inputAnchor: InputAnchor = InputAnchor(this, Type.IMAGE)
    val outputAnchor: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    enum class Method {
        D90, DR90, Flip
    }

    var method: Method? = null

    @FXML
    var inputSpace: VBox? = null

    @FXML
    var outputSpace: VBox? = null

    @FXML
    var methodComboBox: ComboBox<String>? = null

    init {
        inputSpace?.children?.add(0, inputAnchor)
        outputSpace?.children?.add(outputAnchor)

        methodComboBox?.selectionModel?.select(0)
        method = Method.D90

        methodComboBox?.selectionModel?.selectedItemProperty()?.addListener { observable, oldValue, newValue ->
            method = getMethod(newValue)
            update()
        }
    }

    private fun getMethod(value: String): Method {
        when {
            value.contains("-90 degree") -> return Method.DR90
            value.contains("90 degree") -> return Method.D90
            value.contains("180 degree") -> return Method.Flip
            else -> throw IllegalArgumentException("Wrong method name!")
        }
    }

    fun degree(): Int {
        when (method) {
            Method.D90 -> return 90
            Method.DR90 -> return -90
            Method.Flip -> return 180
            else -> throw IllegalArgumentException()
        }
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        return ImmutableList.of(inputAnchor)
    }

    override fun getAllOutputs(): MutableList<OutputAnchor> {
        return ImmutableList.of(outputAnchor)
    }

    override fun update() {
        inputAnchor.invalidateVisualState()
        outputAnchor.invalidateVisualState()
        if (inputAnchor.oppositeAnchor.isPresent) {
            val oppositeAnchor = inputAnchor.oppositeAnchor.get()
            val position = oppositeAnchor.position
            val block = oppositeAnchor.block
            if (block is ValueBlock<*>) {
                val value = block.getValue(position) as FastImage
                val grayFilter = ImageRotation(value)
                Single.fromCallable { grayFilter.rotate(degree()) }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.trampoline())
                        .subscribe(
                                { image ->
                                    log.info("Sending message downstream from RotateBlock: {}", hashCode())
                                    value1 = image
                                    sendUpdateDownSteam()

                                },
                                { log.error("Can`t process image!", it) }
                        )
            }
        }
    }

    override fun getValue(position: Int): FastImage {
        when (position) {
            0 -> return value1
        }
        throw IllegalArgumentException("Wrong position!")
    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}