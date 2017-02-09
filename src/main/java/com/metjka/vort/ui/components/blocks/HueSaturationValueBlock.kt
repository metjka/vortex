package com.metjka.vort.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.precessing.ContrastFilter
import com.metjka.vort.precessing.FastImage
import com.metjka.vort.precessing.HueSaturationValueFilter
import com.metjka.vort.ui.ToplevelPane
import com.metjka.vort.ui.Type
import com.metjka.vort.ui.components.connections.InputAnchor
import com.metjka.vort.ui.components.connections.OutputAnchor
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.layout.VBox
import mu.KotlinLogging
import rx.Single
import rx.schedulers.Schedulers
import java.util.*

class HueSaturationValueBlock(val toplevelPane: ToplevelPane) : ValueBlock<FastImage>(toplevelPane, HueSaturationValueBlock::class.simpleName) {

    val log = KotlinLogging.logger { }

    val inputAnchor: InputAnchor = InputAnchor(this, Type.IMAGE)
    val outputAnchor: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    var hue: Int = 0
    var saturation: Int = 100
    var value: Int = 100

    @FXML
    var inputSpace: VBox? = null

    @FXML
    var outputSpace: VBox? = null

    @FXML
    var hueSlider: Slider? = null

    @FXML
    var saturationSlider: Slider? = null

    @FXML
    var valueSlider: Slider? = null

    @FXML
    var hueLabel: Label? = null

    @FXML
    var saturationLabel: Label? = null

    @FXML
    var valueLabel: Label? = null

    init {
        inputSpace?.children?.add(0, inputAnchor)
        outputSpace?.children?.add(outputAnchor)

        hueLabel?.text = hue.toString()
        saturationLabel?.text = saturation.toString()
        valueLabel?.text = value.toString()

        hueSlider?.value = hue.toDouble()
        saturationSlider?.value = saturation.toDouble()
        valueSlider?.value = value.toDouble()

        hueSlider?.valueChangingProperty()?.addListener { _, _, changing ->
            if (!changing) {
                hue = hueSlider?.value?.toInt()!!
                hueLabel?.text = hue.toString()
                update()
            }
        }

        saturationSlider?.valueChangingProperty()?.addListener { _, _, changing ->
            if (!changing) {
                saturation = saturationSlider?.value?.toInt()!!
                saturationLabel?.text = saturation.toString()
                update()
            }
        }

        valueSlider?.valueChangingProperty()?.addListener { _, _, changing ->
            if (!changing) {
                value = valueSlider?.value?.toInt()!!
                valueLabel?.text = value.toString()
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
                val fastImage = block.getValue(oppositeAnchor.position) as FastImage
                val sob = HueSaturationValueFilter(fastImage)

                Single.fromCallable { sob.filter(hue, saturation, value) }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.trampoline())
                        .subscribe(
                                { image ->
                                    log.info("Sending message downstream from HueSaturationValueBlock: {}", hashCode())
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