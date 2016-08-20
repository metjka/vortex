package pa;

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
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

        Attribute.Layout layout = new RandomLayout(graph);
        layout.execute();

    }

    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("pa.Cell A", CellType.RECTANGLE);
        model.addCell("pa.Cell B", CellType.RECTANGLE);
        model.addCell("pa.Cell C", CellType.RECTANGLE);
        model.addCell("pa.Cell D", CellType.TRIANGLE);
        model.addCell("pa.Cell E", CellType.TRIANGLE);
        model.addCell("pa.Cell F", CellType.RECTANGLE);
        model.addCell("pa.Cell G", CellType.RECTANGLE);

        model.addEdge("pa.Cell A", "pa.Cell B");
        model.addEdge("pa.Cell A", "pa.Cell C");
        model.addEdge("pa.Cell B", "pa.Cell C");
        model.addEdge("pa.Cell C", "pa.Cell D");
        model.addEdge("pa.Cell B", "pa.Cell E");
        model.addEdge("pa.Cell D", "pa.Cell F");
        model.addEdge("pa.Cell D", "pa.Cell G");

        graph.endUpdate();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
