package com.metjka.vort.ui.components.blocks

import com.google.common.collect.ImmutableList
import com.metjka.vort.precessing.ContrastFilter
import com.metjka.vort.precessing.FastImage
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

class ContrastBlock(val toplevelPane: ToplevelPane) : ValueBlock<FastImage>(toplevelPane, ContrastBlock::class.simpleName) {

    val log = KotlinLogging.logger { }

    val inputAnchor: InputAnchor = InputAnchor(this, Type.IMAGE)
    val outputAnchor: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    var contrastFilter: Int = 0

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

        valLavel?.text = contrastFilter.toString()

        briSlider?.valueProperty()?.addListener { observable, oldValue, newValue ->
            contrastFilter = newValue.toInt()
            valLavel?.text = contrastFilter.toString()
            println(contrastFilter)
            update()
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
                val sob = ContrastFilter(value, contrastFilter)
                Single.fromCallable { sob.filter() }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.trampoline())
                        .subscribe(
                                { image ->
                                    log.info("Sending message downstream from ContrastBlock: {}", hashCode())
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