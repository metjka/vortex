package com.metjka.vort.ui.components.blocks

import com.metjka.vort.precessing.BlurProcessing
import com.metjka.vort.precessing.FastImage
import com.metjka.vort.ui.ToplevelPane
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import mu.KotlinLogging
import java.util.*

class BlurBlock(toplevelPane: ToplevelPane) : ValueBlock<FastImage>(toplevelPane, "BlurBlock") {

    val log = KotlinLogging.logger { }

    val single: Single<FastImage>

    init {
        val blurProcessing = BlurProcessing(value1)
        single = Single.fromCallable { blurProcessing.blur(BlurProcessing.BOX_BLUR) }
                .map { it -> it }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.from { this })

    }

    override fun getValue(position: Int): FastImage {
        when (position) {
            0 -> return value1
        }
        throw IllegalArgumentException("Wrong position!")
    }

    override fun update() {
        single.subscribe(
                { fastImage ->
                    value1 = fastImage
                    this.sendUpdateDownSteam()
                },
                { log.error("Can`t process image!", it) }
        )
    }

    override fun getNewCopy(): Optional<Block> {
        throw UnsupportedOperationException("not implemented")
    }
}