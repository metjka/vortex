package com.metjka.vort;

import javafx.application.Application;
import javafx.scene.Scene;
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

        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root, 1024, 768);
//        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

        Layout layout = new RandomLayout(graph);
        layout.execute();

    }

    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("pa.Node A", CellType.RECTANGLE);
        model.addCell("pa.Node B", CellType.RECTANGLE);
        model.addCell("pa.Node C", CellType.RECTANGLE);
        model.addCell("pa.Node D", CellType.TRIANGLE);
        model.addCell("pa.Node E", CellType.TRIANGLE);
        model.addCell("pa.Node F", CellType.RECTANGLE);
        model.addCell("pa.Node G", CellType.RECTANGLE);

        model.addEdge("pa.Node A", "pa.Node B");
        model.addEdge("pa.Node A", "pa.Node C");
        model.addEdge("pa.Node B", "pa.Node C");
        model.addEdge("pa.Node C", "pa.Node D");
        model.addEdge("pa.Node B", "pa.Node E");
        model.addEdge("pa.Node D", "pa.Node F");
        model.addEdge("pa.Node D", "pa.Node G");

        graph.endUpdate();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
