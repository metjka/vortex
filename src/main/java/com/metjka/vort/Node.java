package com.metjka.vort;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;

public class Node extends Pane {

    String cellId;

    List<Node> children = new ArrayList<>();
    List<Node> parents = new ArrayList<>();

    javafx.scene.Node view;

    public Node(String cellId) {
        this.cellId = cellId;
    }

    public void addCellChild(Node cell) {
        children.add(cell);
    }

    public List<Node> getCellChildren() {
        return children;
    }

    public void addCellParent(Node cell) {
        parents.add(cell);
    }

    public List<Node> getCellParents() {
        return parents;
    }

    public void removeCellChild(Node cell) {
        children.remove(cell);
    }

    public void setView(javafx.scene.Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public javafx.scene.Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }
}
