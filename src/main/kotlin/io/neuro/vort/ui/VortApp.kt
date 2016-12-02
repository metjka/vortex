package io.neuro.vort.ui

import io.neuro.vort.ui.Style.Companion.processingScreen
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import tornadofx.*

class VortApp : App() {

    override fun start(stage: Stage) {
        stage.width = 1024.0
        stage.height = 768.0

        val topLevelPane = TopLevelPane()
        val scene = Scene(topLevelPane)

        stage.scene = scene


        super.start(stage)
    }
}

fun main(args: Array<String>) {
    Application.launch(VortApp::class.java, *args)
}

class MainOverLay : StackPane(){



}

class ProcessingScreen : View() {
    override val root = GridPane()

}

class TopLevelPane() : Region() {
    val botomLayer:Pane
    val blockLayer:Pane
    val wireLayer: Pane

    init {
        botomLayer = Pane()
        blockLayer = Pane(botomLayer)
        wireLayer = Pane(blockLayer)
        this.children.add(wireLayer)
    }

}

class ProcessingController : Controller() {
    val processingScreen: ProcessingScreen by inject()
    fun init() {

    }
}