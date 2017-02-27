package io.metjka.vortex.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.precessing.FastImage
import com.metjka.vort.precessing.GammaFilter
import io.metjka.vortex.ui.ToplevelPane
import io.metjka.vortex.ui.Type
import io.metjka.vortex.ui.components.connections.InputAnchor
import io.metjka.vortex.ui.components.connections.OutputAnchor
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.layout.VBox
import mu.KotlinLogging
import rx.Single
import rx.schedulers.Schedulers
import java.util.*

class GammaBlock(val toplevelPane: ToplevelPane) : ValueBlock<FastImage>(toplevelPane, GammaBlock::class.simpleName) {

    val log = KotlinLogging.logger { }

    val inputAnchor: InputAnchor = InputAnchor(this, Type.IMAGE)
    val outputAnchor: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    var brightnessValue: Double = 0.05

    @FXML
    var inputSpace: VBox? = null

    @FXML
    var outputSpace: VBox? = null

    @FXML
    var briSlider: Slider? = null

    @FXML
    var valLavel: Label? = null

    init {
        inputSpace?.children?.add(0, inputAnchor)
        outputSpace?.children?.add(outputAnchor)

        valLavel?.text = brightnessValue.toString()

        briSlider?.valueChangingProperty()?.addListener { observableValue, wasChanging, changing ->
            if (!changing) {
                brightnessValue = briSlider?.value!!
                valLavel?.text = brightnessValue.toString()
                update()
            }
        }
    }

    override fun update() {
        inputAnchor.invalidateVisualState()
        outputAnchor.invalidateVisualState()

        if (inputAnchor.oppositeAnchor.isPresent) {
            val oppositeAnchor = inputAnchor.oppositeAnchor.get()
            val block = oppositeAnchor.block
            if (block is ValueBlock<*>) {
                val value = block.getValue(oppositeAnchor.position) as FastImage
                val sob = GammaFilter(value, brightnessValue)
                Single.fromCallable { sob.filter() }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.trampoline())
                        .subscribe(
                                { image ->
                                    log.info("Sending message downstream from GammaBlock: {}", hashCode())
                                    value1 = image
                                    sendUpdateDownSteam()

                                },
                                { log.error("Can`t process image!", it) }
                        )

            }

        }
    }

    override fun getAllOutputs(): MutableList<OutputAnchor> {
        return ImmutableList.of(outputAnchor)
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        return ImmutableList.of(inputAnchor)
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