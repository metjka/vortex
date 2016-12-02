package io.neuro.vort.ui

import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class Style : Stylesheet() {

    companion object{
        val processingScreen by cssclass()
    }

    init {
        select(processingScreen){
        }
    }
}