package com.metjka.vort.old;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 * Created by metka on 14.8.2016.
 */
public class VortApplication extends Application {
    Graph graph = new Graph();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        graph = new Graph();
        ScrollPane scrollPane = graph.getScrollPane();

        root.setCenter(scrollPane);


        Scene scene = new Scene(root, 1024, 768);
//        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

        RandomLayout layout = new RandomLayout(graph);
        layout.execute();
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) {

                graph.getModel().addCell("ad", CellType.RECTANGLE);
                graph.endUpdate();
//                layout.execute();
            }});
    }

    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("com.metjka.vort.old.ui.pa.Node A", CellType.RECTANGLE);
        model.addCell("com.metjka.vort.old.ui.pa.Node B", CellType.RECTANGLE);
        model.addCell("com.metjka.vort.old.ui.pa.Node C", CellType.RECTANGLE);
        model.addCell("com.metjka.vort.old.ui.pa.Node D", CellType.TRIANGLE);
        model.addCell("com.metjka.vort.old.ui.pa.Node E", CellType.TRIANGLE);
        model.addCell("com.metjka.vort.old.ui.pa.Node F", CellType.RECTANGLE);
        model.addCell("com.metjka.vort.old.ui.pa.Node G", CellType.RECTANGLE);

        model.addEdge("com.metjka.vort.old.ui.pa.Node A", "com.metjka.vort.old.ui.pa.Node B");
        model.addEdge("com.metjka.vort.old.ui.pa.Node A", "com.metjka.vort.old.ui.pa.Node C");
        model.addEdge("com.metjka.vort.old.ui.pa.Node B", "com.metjka.vort.old.ui.pa.Node C");
        model.addEdge("com.metjka.vort.old.ui.pa.Node C", "com.metjka.vort.old.ui.pa.Node D");
        model.addEdge("com.metjka.vort.old.ui.pa.Node B", "com.metjka.vort.old.ui.pa.Node E");
        model.addEdge("com.metjka.vort.old.ui.pa.Node D", "com.metjka.vort.old.ui.pa.Node F");
        model.addEdge("com.metjka.vort.old.ui.pa.Node D", "com.metjka.vort.old.ui.pa.Node G");

        graph.endUpdate();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
