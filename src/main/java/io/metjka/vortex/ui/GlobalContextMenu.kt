package io.metjka.vortex.ui

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem

/**
 * A context menu with global actions (i.e. quit).
 */
class GlobalContextMenu(menuActions: MenuActions) : ContextMenu() {
    init {


        val menuFullScreen = MenuItem("Toggle full screen")
        menuFullScreen.onAction = EventHandler { actionEvent -> menuActions.toggleFullScreen(actionEvent) }

        val menuQuit = MenuItem("Quit")
        menuQuit.onAction = EventHandler { actionEvent -> menuActions.onQuit(actionEvent) }

    }
}
