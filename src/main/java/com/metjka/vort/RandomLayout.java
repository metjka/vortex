package com.metjka.vort;

import java.util.List;
import java.util.Random;

/**
 * Created by Ihor Salnikov on 20.8.2016, 9:18 PM.
 * https://github.com/metjka/VORT
 */
public class RandomLayout extends Layout {

    Graph graph;

    Random rnd = new Random();

    public RandomLayout(Graph graph) {

        this.graph = graph;

    }

    public void execute() {

        List<Node> cells = graph.getModel().getAllCells();

        for (Node cell : cells) {

            double x = rnd.nextDouble() * 500;
            double y = rnd.nextDouble() * 500;

            cell.relocate(x, y);

        }
    }
}
