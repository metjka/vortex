package io.metjka.vortex.ui.blocks

import com.google.common.collect.ImmutableList
import io.metjka.vortex.precessing.FastImage
import io.metjka.vortex.precessing.InvertFilter
import io.metjka.vortex.ui.TopLevelPane
import io.metjka.vortex.ui.connections.InputAnchor
import io.metjka.vortex.ui.connections.OutputAnchor
import javafx.fxml.FXML
import javafx.scene.layout.VBox
import mu.KotlinLogging
import rx.Single
import rx.schedulers.Schedulers
import java.util.*

class InvertBlock(topLevelPane: TopLevelPane) : Block(topLevelPane, "InvertBlock") {

    val log = KotlinLogging.logger { }

    val inputAnchor: InputAnchor = InputAnchor(this, Type.IMAGE)
    val outputAnchor: OutputAnchor = OutputAnchor(this, 0, Type.IMAGE)

    @FXML
    var inputSpace: VBox? = null

    @FXML
    var outputSpace: VBox? = null

    init {
        inputSpace?.children?.add(0, inputAnchor)
        outputSpace?.children?.add(outputAnchor)
    }


    override fun getAllOutputs(): MutableList<OutputAnchor> {
        return ImmutableList.of(outputAnchor)
    }

    override fun getAllInputs(): MutableList<InputAnchor> {
        return ImmutableList.of(inputAnchor)
    }

    override fun update() {
        inputAnchor.invalidateVisualState()
        outputAnchor.invalidateVisualState()
        log.info("On update in InvertBlock: {}", hashCode())

        if (inputAnchor.oppositeAnchor.isPresent) {
            val oppositeAnchor = inputAnchor.oppositeAnchor.get()
            val block = oppositeAnchor.block
            if (block is ValueBlock<*>) {
                val value = block.getValue(oppositeAnchor.position) as FastImage
                val filter = InvertFilter(value)
                Single.fromCallable { filter.filter() }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.trampoline())
                        .subscribe(
                                { image ->
                                    log.info("Sending message downstream from ConvolutionBlock: {}", hashCode())
                                    value1 = image
                                    sendUpdateDownSteam()

                                },
                                { log.error("Can`t process image!", it) }
                        )
            }

        }
    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getValue(position: Int): FastImage {
        when (position) {
            0 -> return value1
        }
        throw IllegalArgumentException("Wrong position!")
    }
}

