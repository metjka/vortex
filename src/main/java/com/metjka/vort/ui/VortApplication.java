package com.metjka.vort.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

/**
 * VortApplication application class for the GUI.
 */
public class VortApplication extends Application {
    /** A reference to the main window */
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Font.loadFont(this.getClass().getResourceAsStream("/ui/fonts/titillium.otf"), 20);

        ToplevelPane toplevelPane = new ToplevelPane();
        MainOverlay overlay = new MainOverlay(toplevelPane);
        Scene scene = new Scene(overlay);
        Preferences prefs = Preferences.userNodeForPackage(VortApplication.class);
        String backGroundImage = prefs.get("background", "/ui/grid.png");
        toplevelPane.setStyle("-fx-background-image: url('" + backGroundImage + "');");
        String theme = prefs.get("theme", "/ui/colours.css");
        scene.getStylesheets().addAll("/ui/layout.css", theme);

        stage.setWidth(1024);
        stage.setHeight(768);

        stage.setOnCloseRequest(event -> System.exit(0));
        stage.setScene(scene);

        stage.show();
        toplevelPane.requestFocus();
        
    }

    /**
     * Get the main stage for this app
     *
     * @return the current stage
     */
    public static Stage getStage() {
        return primaryStage;
    }

    /**
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
