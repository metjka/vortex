package com.metjka.vort.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class VortApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ToplevelPane toplevelPane = new ToplevelPane();
        MainOverlay overlay = new MainOverlay(toplevelPane);
        Scene scene = new Scene(overlay);

        Preferences prefs = Preferences.userNodeForPackage(VortApp.class);
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

    public static void main(String[] args) {
        launch(VortApp.class, args);
    }
}
