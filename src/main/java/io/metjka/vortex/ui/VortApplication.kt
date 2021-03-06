package io.metjka.vortex.ui

import io.metjka.vortex.ui.blocks.MathBlock
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.text.Font
import javafx.stage.Stage
import java.io.IOException
import java.util.prefs.Preferences

/**
 * Created by Ihor Salnikov on 24.2.2017, 9:53 PM.
 * https://github.com/metjka/VORT
 */
class VortApplication : Application() {

    companion object {

        var stagee: Stage? = null

        @JvmStatic
        fun main(args: Array<String>) {
            launch(VortApplication::class.java, *args)
        }
    }

    override fun start(primaryStage: Stage?) {

        stagee = primaryStage

        Font.loadFont(this::class.java.getResourceAsStream("/ui/fonts/FiraCode-Light.otf"), 20.0)

        val toplevelPane = TopLevelPane()
        val mainOverlay = MainOverlay(toplevelPane)
        val scene = Scene(mainOverlay)
        val prefs = Preferences.userNodeForPackage(VortApplication::class.java)
        val background = prefs.get("background", "/ui/grid.png")
        toplevelPane.style = "-fx-background-image: url('$background');"
        val theme = prefs.get("theme", "/ui/colours.css")
        scene.stylesheets.addAll("/ui/layout.css", theme)

        primaryStage?.width = 1024.0
        primaryStage?.height = 768.0
        primaryStage?.scene = scene
        primaryStage?.setOnCloseRequest { System.exit(0) }

        primaryStage?.show()

        toplevelPane.requestFocus()

        toplevelPane.addBlock(MathBlock(toplevelPane))
        initApp(toplevelPane)
    }

    fun initApp(toplevelPane: TopLevelPane) {
        toplevelPane.addBlock(MathBlock(toplevelPane))
    }
}


fun Node.loadXML(name: String) {

    try {
        val url = javaClass.getResource(String.format("/ui/%s.fxml", name))
        val fxmlLoader = FXMLLoader(url)
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)

        fxmlLoader.load<Any>()
    } catch (e: IOException) {
        throw RuntimeException("A required FXML file, $name, could not be loaded.", e)
    }


}