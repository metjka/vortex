package io.metjka.vortex.ui.blocks

import com.google.common.collect.ImmutableList
import io.metjka.vortex.precessing.Convolution
import io.metjka.vortex.precessing.FastImage
import io.metjka.vortex.precessing.Kernel
import io.metjka.vortex.ui.ToplevelPane
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.layout.VBox
import mu.KotlinLogging
import rx.Single
import rx.schedulers.Schedulers
import java.util.*

class ConvolutionBlock(toplevelPane: ToplevelPane) : ValueBlock<FastImage>(toplevelPane, "ConvolutionBlock") {

    val log = KotlinLogging.logger { }

    val inputAnchor: InputAnchor = InputAnchor(this, Type.IMAGE)
    val outputAnchor: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    var method: Method? = null

    enum class Method {
        BOX_BLUR, GAUSSIAN_3, GAUSSIAN_5, SOBEL_HORIZONTAL, SOBEL_VERTICAL, SHARPEN, LAPLACE, LAP
    }

    @FXML
    var methodComboBox: ComboBox<String>? = null

    @FXML
    var inputSpace: VBox? = null

    @FXML
    var outputSpace: VBox? = null

    init {
        inputSpace?.children?.add(0, inputAnchor)
        outputSpace?.children?.add(outputAnchor)

        methodComboBox?.selectionModel?.select(0)
        method = Method.BOX_BLUR

        methodComboBox?.selectionModel?.selectedItemProperty()?.addListener { observableValue, oldValue, newValue ->
            method = getMethod(newValue)
            update()
        }

    }

    private fun getMethod(method: String): Method {
        when (method) {
            "BOX BLUR" -> return Method.BOX_BLUR
            "GAUSSIAN3" -> return Method.GAUSSIAN_3
            "GAUSSIAN5" -> return Method.GAUSSIAN_5
            "SHARPEN" -> return Method.SHARPEN
            "SOBEL_HORIZONTAL" -> return Method.SOBEL_HORIZONTAL
            "SOBEL_VERTICAL" -> return Method.SOBEL_VERTICAL
            "LAPLACE" -> return Method.LAPLACE
            "LAP" -> return Method.LAP
            else -> throw IllegalArgumentException("Wrong method name!")
        }
    }

    fun getKernel(): Kernel {
        when (method) {
            Method.BOX_BLUR -> return Convolution.BOX_BLUR
            Method.GAUSSIAN_3 -> return Convolution.GAUSSIAN3_BLUR
            Method.GAUSSIAN_5 -> return Convolution.GAUSSIAN5_BLUR
            Method.SHARPEN -> return Convolution.SHARPEN
            Method.SOBEL_HORIZONTAL -> return Convolution.SOBEL_HORIZONTAL
            Method.SOBEL_VERTICAL -> return Convolution.SOBEL_VERTICAL
            Method.LAPLACE -> return Convolution.LAPLACE
            Method.LAP -> return Convolution.LAP
            else -> throw IllegalArgumentException("Wrong method!")
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
                val blurProcessing = Convolution(value)
                Single.fromCallable { blurProcessing.convolve(getKernel()) }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.trampoline())
                        .subscribe(
                                { image ->
                                    log.info("Sending message downstream from ConvolutionBlock: {}"
                                            , hashCode())
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
