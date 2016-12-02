package io.neuro.vort.image.porcessing

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {


    override fun start(primaryStage: Stage?) {

        val nodeNodeContainer: NodeContainer = NodeContainer()
        nodeNodeContainer.id = "pane"
        val scene = Scene(nodeNodeContainer, 1024.0, 768.0)

        scene.stylesheets.addAll(this.javaClass.classLoader.getResource("style.css").toExternalForm());

        primaryStage?.scene = scene
        primaryStage?.show()


    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }

}


