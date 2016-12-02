package com.metjka.vort.old.ui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;

/**
 * Created by Ihor Salnikov on 5.11.2016, 2:19 PM.
 * https://github.com/metjka/VORT
 */
public class NodeGraphTest extends Application {

    public static void main(String[] args) { launch(args); }

    @Override public void start(final Stage stage) {
        final Group group = new Group(
                createStar(),
                createCurve()
        );

        Parent zoomPane = createZoomPane(group);

        VBox layout = new VBox();
        layout.getChildren().setAll(
                createMenuBar(stage, group),
                zoomPane
        );

        VBox.setVgrow(zoomPane, Priority.ALWAYS);
        Scene scene = new Scene(
                layout
        );

        stage.setTitle("Zoomy");
        stage.getIcons().setAll(new Image(APP_ICON));
        stage.setScene(scene);
        stage.show();
    }

    private Parent createZoomPane(final Group group) {
        final double SCALE_DELTA = 1.1;
        final StackPane zoomPane = new StackPane();

        zoomPane.getChildren().add(group);
        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor =
                        (event.getDeltaY() > 0)
                                ? SCALE_DELTA
                                : 1/SCALE_DELTA;

                group.setScaleX(group.getScaleX() * scaleFactor);
                group.setScaleY(group.getScaleY() * scaleFactor);
            }
        });

        zoomPane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) {
                zoomPane.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
            }
        });

        return zoomPane;
    }

    private SVGPath createCurve() {
        SVGPath ellipticalArc = new SVGPath();
        ellipticalArc.setContent(
                "M10,150 A15 15 180 0 1 70 140 A15 25 180 0 0 130 130 A15 55 180 0 1 190 120"
        );
        ellipticalArc.setStroke(Color.LIGHTGREEN);
        ellipticalArc.setStrokeWidth(4);
        ellipticalArc.setFill(null);
        return ellipticalArc;
    }

    private SVGPath createStar() {
        SVGPath star = new SVGPath();
        star.setContent(
                "M100,10 L100,10 40,180 190,60 10,60 160,180 z"
        );
        star.setStrokeLineJoin(StrokeLineJoin.ROUND);
        star.setStroke(Color.BLUE);
        star.setFill(Color.DARKBLUE);
        star.setStrokeWidth(4);
        return star;
    }

    private MenuBar createMenuBar(final Stage stage, final Group group) {
        Menu fileMenu = new Menu("_File");
        MenuItem exitMenuItem = new MenuItem("E_xit");
        exitMenuItem.setGraphic(new ImageView(new Image(CLOSE_ICON)));
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                stage.close();
            }
        });
        fileMenu.getItems().setAll(
                exitMenuItem
        );
        Menu zoomMenu = new Menu("_Zoom");
        MenuItem zoomResetMenuItem = new MenuItem("Zoom _Reset");
        zoomResetMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE));
        zoomResetMenuItem.setGraphic(new ImageView(new Image(ZOOM_RESET_ICON)));
        zoomResetMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                group.setScaleX(1);
                group.setScaleY(1);
            }
        });
        MenuItem zoomInMenuItem = new MenuItem("Zoom _In");
        zoomInMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.I));
        zoomInMenuItem.setGraphic(new ImageView(new Image(ZOOM_IN_ICON)));
        zoomInMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                group.setScaleX(group.getScaleX() * 1.5);
                group.setScaleY(group.getScaleY() * 1.5);
            }
        });
        MenuItem zoomOutMenuItem = new MenuItem("Zoom _Out");
        zoomOutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O));
        zoomOutMenuItem.setGraphic(new ImageView(new Image(ZOOM_OUT_ICON)));
        zoomOutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                group.setScaleX(group.getScaleX() * 1/1.5);
                group.setScaleY(group.getScaleY() * 1/1.5);
            }
        });
        zoomMenu.getItems().setAll(
                zoomResetMenuItem,
                zoomInMenuItem,
                zoomOutMenuItem
        );
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().setAll(
                fileMenu,
                zoomMenu
        );
        return menuBar;
    }

    // icons source from: http://www.iconarchive.com/show/soft-scraps-icons-by-deleket.html
    // icon license: CC Attribution-Noncommercial-No Derivate 3.0 =? http://creativecommons.org/licenses/by-nc-nd/3.0/
    // icon Commercial usage: Allowed (Author Approval required -> Visit artist website for details).

    public static final String APP_ICON        = "http://icons.iconarchive.com/icons/deleket/soft-scraps/128/Zoom-icon.png";
    public static final String ZOOM_RESET_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Zoom-icon.png";
    public static final String ZOOM_OUT_ICON   = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Zoom-Out-icon.png";
    public static final String ZOOM_IN_ICON    = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Zoom-In-icon.png";
    public static final String CLOSE_ICON      = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Button-Close-icon.png";
}

