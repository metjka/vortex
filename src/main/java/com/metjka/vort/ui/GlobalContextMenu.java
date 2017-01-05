package com.metjka.vort.ui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * A context menu with global actions (i.e. quit).
 */
public class GlobalContextMenu extends ContextMenu {
    public GlobalContextMenu(MenuActions menuActions) {
        super();


        MenuItem menuFullScreen = new MenuItem("Toggle full screen");
        menuFullScreen.setOnAction(menuActions::toggleFullScreen);
        
        MenuItem menuQuit = new MenuItem("Quit");
        menuQuit.setOnAction(menuActions::onQuit);

    }
}
