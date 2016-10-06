package com.metjka.vort;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * Created by Ihor Salnikov on 28.8.2016, 2:36 PM.
 * https://github.com/metjka/VORT
 */
public class AppTest extends Application {
    public static List<Circle> circles = new ArrayList<Circle>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();

        Canvas canvas = new Canvas(300, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //drawShapes(gc);

        Circle circle1 = new Circle(50);
        circle1.setStroke(Color.GREEN);
        circle1.setFill(Color.GREEN.deriveColor(1, 1, 1, 0.7));
        circle1.relocate(100, 100);

        Circle circle2 = new Circle(50);
        circle2.setStroke(Color.BLUE);
        circle2.setFill(Color.BLUE.deriveColor(1, 1, 1, 0.7));
        circle2.relocate(200, 200);

//        Line line = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
        Line line = new Line();
        line.startXProperty().bind(circle1.layoutXProperty().add(circle1.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind(circle1.layoutYProperty().add(circle1.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind(circle2.layoutXProperty().add(circle2.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind(circle2.layoutYProperty().add(circle2.getBoundsInParent().getHeight() / 2.0));
        line.setStrokeWidth(20);


        Pane overlay = new Pane();
        overlay.getChildren().addAll(circle1, circle2, line);

        MouseGestures mg = new MouseGestures();
        mg.makeDraggable(circle1);
        mg.makeDraggable(circle2);
//        mg.makeDraggable(line);

        root.getChildren().addAll(canvas, overlay);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.strokeRoundRect(10, 10, 230, 230, 10, 10);
    }

    public static class MouseGestures {

        double orgSceneX, orgSceneY;
        double orgTranslateX, orgTranslateY;

        public void makeDraggable(Node node) {
            node.setOnMousePressed(circleOnMousePressedEventHandler);
            node.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        }

        EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {

                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();

                if (t.getSource() instanceof Circle) {

                    Circle p = ((Circle) (t.getSource()));

                    orgTranslateX = p.getCenterX();
                    orgTranslateY = p.getCenterY();

                } else {

                    Node p = ((Node) (t.getSource()));

                    orgTranslateX = p.getTranslateX();
                    orgTranslateY = p.getTranslateY();

                }
            }
        };

        EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {

                double offsetX = t.getSceneX() - orgSceneX;
                double offsetY = t.getSceneY() - orgSceneY;

                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                if (t.getSource() instanceof Circle) {

                    Circle p = ((Circle) (t.getSource()));

                    p.setCenterX(newTranslateX);
                    p.setCenterY(newTranslateY);

                } else {

                    Node p = ((Node) (t.getSource()));

                    p.setTranslateX(newTranslateX);
                    p.setTranslateY(newTranslateY);

                }

            }
        };

    }
}
