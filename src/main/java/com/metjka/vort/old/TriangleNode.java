package com.metjka.vort.old;



import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class TriangleNode extends Node {

    public TriangleNode(String id) {
        super(id);

        double width = 50;
        double height = 50;

        Polygon view = new Polygon(width / 2, 0, width, height, 0, height);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);

        setView(view);

    }

}