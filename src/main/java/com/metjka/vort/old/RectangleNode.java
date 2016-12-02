package com.metjka.vort.old;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleNode extends Node {

    public RectangleNode(String id) {
        super( id);

        Rectangle view = new Rectangle( 50,50);

        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

        setView( view);

    }

}