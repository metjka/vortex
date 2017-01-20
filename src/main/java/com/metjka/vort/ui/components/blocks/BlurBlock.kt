package com.metjka.vort.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.precessing.BlurProcessing
import com.metjka.vort.precessing.FastImage
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
import tornadofx.ellipse
import java.util.*
import java.util.function.Predicate
import java.util.function.Supplier

class BlurBlock(toplevelPane: ToplevelPane) : ValueBlock<FastImage>(toplevelPane, "BlurBlock") {

    val log = KotlinLogging.logger { }

    val inputAnchor: InputAnchor = InputAnchor(this, Type.IMAGE)
    val outputAnchor: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    enum class Method {
        BOX_BLUR, GAUSSIAN_3, GAUSSIAN_5, SOBEL, SHARPEN
    }

    @FXML
    private val methodComboBox: ComboBox<String>? = null

    @FXML
    var inputSpace: VBox? = null

    @FXML
    var outputSpace: VBox? = null

    init {
        inputSpace?.children?.add(0, inputAnchor)
        outputSpace?.children?.add(outputAnchor)

        methodComboBox?.selectionModel?.select(0)

        methodComboBox?.selectionModel?.selectedItemProperty()?.addListener { observableValue, oldValue, newValue ->
            getMethod(newValue)
            newValue.map {  }
        }

    }

    private fun getMethod(method: String): Method {

        when (method) {
            "BOX BLUR" -> return Method.BOX_BLUR
            else -> throw IllegalArgumentException("Wrong method name!")
        }


    }

    override fun update() {
        inputAnchor.invalidateVisualState()
        outputAnchor.invalidateVisualState()
        log.info("On update in BlurBox: {}", hashCode())


        if (inputAnchor.oppositeAnchor.isPresent) {
            val oppositeAnchor = inputAnchor.oppositeAnchor.get()
            val block = oppositeAnchor.block
            if (block is ValueBlock<*>) {
                val value = block.getValue(oppositeAnchor.position) as FastImage
                val blurProcessing = BlurProcessing(value)
                Single.fromCallable { blurProcessing.blur(BlurProcessing.GAUSSIAN5_BLUR) }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.trampoline())
                        .subscribe(
                                { image ->
                                    log.info("Sending message downstream from BlurBlock: {}", hashCode())
                                    value1 = image
                                    sendUpdateDownSteam()

                                },
                                { log.error("Can`t process image!", it) }
                        )
            }

        }
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        return ImmutableList.of(inputAnchor)
    }

    override fun getAllOutputs(): MutableList<OutputAnchor> {
        return ImmutableList.of(outputAnchor)
    }

    override fun getValue(position: Int): FastImage {
        when (position) {
            0 -> return value1
        }
        throw IllegalArgumentException("Wrong position!")
    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented")
    }
}
