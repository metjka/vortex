package com.metjka.vort.old.ui.pa;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
class Ball extends Circle {
    private double dragBaseX;
    private double dragBaseY;

    public Ball(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);

        setOnMousePressed(event -> {
            dragBaseX = event.getSceneX() - getCenterX();
            dragBaseY = event.getSceneY() - getCenterY();
        });

        setOnMouseDragged(event -> {
            setCenterX(event.getSceneX() - dragBaseX);
            setCenterY(event.getSceneY() - dragBaseY);
        });
    }
}

class Connection extends Line {
    public Connection(Ball startBall, Ball endBall) {
        startXProperty().bind(startBall.centerXProperty());
        startYProperty().bind(startBall.centerYProperty());
        endXProperty().bind(endBall.centerXProperty());
        endYProperty().bind(endBall.centerYProperty());
    }
}

public class aa extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();

        initShapes(root);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void initShapes(Group root) {
        Ball ball1 = new Ball(100, 200, 20);
        ball1.setFill(Color.RED);
        root.getChildren().add(ball1);
        Ball ball2 = new Ball(300, 200, 20);
        ball2.setFill(Color.RED);
        root.getChildren().add(ball2);

        Connection connection = new Connection(ball1, ball2);
        connection.setStroke(Color.CYAN);
        connection.setStrokeWidth(5);
        root.getChildren().add(0, connection);
    }

    public static void main(String... args) {
        launch(args);
    }
}