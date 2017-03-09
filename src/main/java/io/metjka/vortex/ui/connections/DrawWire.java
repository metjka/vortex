package io.metjka.vortex.ui.connections;

import io.metjka.vortex.ui.BlockContainer;
import io.metjka.vortex.ui.ComponentLoader;
import io.metjka.vortex.ui.TopLevelPane;
import io.metjka.vortex.ui.WireMenu;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * A DrawWire represents the UI for a new incomplete connection is the process of being drawn.
 * It is linked to a single Anchor as starting point, and with a second anchor it produces a new ConnectionDep.
 */
public class DrawWire extends CubicCurve implements ChangeListener<Transform>, ComponentLoader {

    static final int GOOD_TYPE_REACTION = 1;
    static final int NEUTRAL_TYPE_REACTION = 0;
    static final int WRONG_TYPE_REACTION = -1;
    /**
     * The Anchor this wire is connected to
     */
    protected final ConnectionAnchor anchor;

    private final TouchArea toucharea;

    private WireMenu menu;

    /**
     * @param anchor        the connected side of this new wire.
     * @param startingPoint the position where this wire was initiated from.
     * @param touchPoint    that initiated this wire, or null if it was by mouse.
     */
    private DrawWire(ConnectionAnchor anchor, Point2D startingPoint, TouchPoint touchPoint) {
        this.setMouseTransparent(true);
        this.anchor = anchor;
        this.anchor.setWireInProgress(this);

        TopLevelPane pane = anchor.getTopLevelPane();
        pane.addWire(this);
        this.setFreePosition(startingPoint);
        anchor.localToSceneTransformProperty().addListener(this);

        this.toucharea = new TouchArea(touchPoint);
        pane.addUpperTouchArea(this.toucharea);
    }

    protected static DrawWire initiate(ConnectionAnchor anchor, TouchPoint touchPoint) {
        if (anchor instanceof InputAnchor && anchor.hasConnection()) {
            ConnectionDep<?> conn = ((InputAnchor<?>) anchor).getConnection().get();
            OutputAnchor startAnchor = conn.getStart();
            if (startAnchor.getWireInProgress() == null) {
                // make room for a new connection by removing existing one
                conn.remove();
                // keep the other endDot of old connection to initiate the new one
                return new DrawWire(startAnchor, anchor.getAttachmentPoint(), touchPoint);
            } else {
                return null;
            }
        } else if (anchor instanceof OutputAnchor<?> && anchor.hasConnection() && ((OutputAnchor<?>) anchor).getConnectionDeps().get(0).hasError()) {
            ConnectionDep<?> conn = ((OutputAnchor<?>) anchor).getConnectionDeps().get(0);
            InputAnchor endAnchor = conn.getEnd();
            if (endAnchor.getWireInProgress() == null) {
                // trying to solve the type error by changing the connection
                conn.remove();
                return new DrawWire(endAnchor, anchor.getAttachmentPoint(), touchPoint);
            }
        }

        return new DrawWire(anchor, anchor.getAttachmentPoint(), touchPoint);
    }

    /**
     * @return the ConnectionAnchor related to the picked Node, or null of none.
     */
    private static ConnectionAnchor findPickedAnchor(Node picked) {
        Node next = picked;
        while (next != null) {
            if (next instanceof Target) {
                return ((Target) next).getAssociatedAnchor();
            }
            next = next.getParent();
        }

        return null;
    }

    public ConnectionAnchor getAnchor() {
        return this.anchor;
    }

    private void showMenu(boolean byMouse) {
        if (this.menu == null) {
            this.menu = new WireMenu(this, byMouse);
            this.menu.relocate(this.toucharea.getLayoutX() + 50, this.toucharea.getLayoutY() - 50);
            this.anchor.getBlock().getTopLevelPane().addMenu(this.menu);
        }
    }

    protected void handleMouseDrag(MouseEvent event) {
        if (this.menu == null && !event.isSynthesized()) {
            Point2D localPos = this.anchor.getTopLevelPane().sceneToLocal(event.getSceneX(), event.getSceneY());
            this.toucharea.dragTo(localPos.getX(), localPos.getY(), event.getPickResult().getIntersectedNode());
        }
        event.consume();
    }

    protected void handleMouseRelease(MouseEvent event) {
        this.toucharea.handleMouseRelease(event);
    }

    private void handleReleaseOn(Node picked) {
        ConnectionAnchor target = findPickedAnchor(picked);
        if (target == this.anchor) {
            this.toucharea.handleReleaseOnSelf();
        } else if (target != null && target.getWireInProgress() == null) {
            ConnectionDep connectionDep = this.buildConnectionTo(target);
            if (connectionDep != null) {
                connectionDep.getStart().handleChange();
                this.remove();
            } else {
                this.toucharea.handleReleaseOnNothing();
            }
        } else if (ConnectionDep.Companion.lengthSquared(this) < 900) {
            this.toucharea.handleReleaseOnSelf();
        } else {
            this.toucharea.handleReleaseOnNothing();
        }
    }

    /**
     * Constructs a new ConnectionDep from this partial wire and another anchor
     *
     * @param target the Anchor to which the other endDot of this should be connection to.
     * @return the newly build ConnectionDep or null if it's not possible
     */
    public ConnectionDep buildConnectionTo(ConnectionAnchor target) {
        InputAnchor sink;
        OutputAnchor source;
        if (this.anchor instanceof InputAnchor) {
            if (target instanceof InputAnchor) {
                return null;
            }
            sink = (InputAnchor) this.anchor;
            source = (OutputAnchor) target;
        } else {
            if (target instanceof OutputAnchor) {
                return null;
            }
            sink = (InputAnchor) target;
            source = (OutputAnchor) this.anchor;

            if (sink.hasConnection()) {
                sink.removeConnections(); // push out the existing connection
            }
        }

        if (sink.getBlock() == source.getBlock()) {
            // self recursive wires are not allowed
            return null;
        }

        return new ConnectionDep(source, sink);
    }

    /**
     * Removes this wire from its pane, and its listener.
     */
    public final void remove() {
        if (this.menu != null) {
            this.menu.close();
            this.menu = null;
        }

        if (this.toucharea != null) {
            this.toucharea.remove();
        }

        this.anchor.setWireInProgress(null);
        this.anchor.localToSceneTransformProperty().removeListener(this);
        this.anchor.getTopLevelPane().removeWire(this);
    }

    @Override
    public void changed(ObservableValue<? extends Transform> observable, Transform oldValue, Transform newValue) {
        this.invalidateAnchorPosition();
    }

    /**
     * Update the UI position of the anchor.
     */
    private void invalidateAnchorPosition() {
        Point2D point = this.anchor.getAttachmentPoint();
        if (this.anchor instanceof InputAnchor) {
            this.setEndX(point.getX());
            this.setEndY(point.getY());

        } else {
            this.setStartX(point.getX());
            this.setStartY(point.getY());
        }

        ConnectionDep.Companion.updateBezierControlPoints(this);
    }

    /**
     * Sets the free endDot coordinates for this wire.
     *
     * @param point coordinates local to this wire's parent.
     */
    public void setFreePosition(Point2D point) {
        if (this.anchor instanceof InputAnchor) {
            this.setStartX(point.getX());
            this.setStartY(point.getY());

        } else {
            this.setEndX(point.getX());
            this.setEndY(point.getY());
        }

        this.invalidateAnchorPosition();

        TopLevelPane pane = this.anchor.getBlock().getTopLevelPane();
        Point2D scenePoint = pane.localToScene(point, false);
        BlockContainer anchorContainer = this.anchor.getContainer();
        boolean scopeOK = true;

        this.getStrokeDashArray().clear();

    }

    /**
     * A circular area at the open endDot of the draw wire for handling multi finger touch actions.
     * This area has as a workaround a hole in the middle to be able to pick the thing behind it on release.
     */
    private class TouchArea extends Path {
        /**
         * Timed animation for toucharea and drawwire self removal
         */
        private final Timeline disapperance;
        /**
         * The ID of finger that spawned this touch area.
         */
        private int touchID;
        /**
         * Whether this touch area has been dragged further than the drag threshold.
         */
        private boolean dragStarted;
        /**
         * List of nearby anchors that have visually reacted to this wire.
         */
        private List<ConnectionAnchor> nearbyAnchors;

        private Point2D lastNearbyUpdate;

        /**
         * @param touchPoint that is the center of new active touch area, or null if the mouse
         */
        private TouchArea(TouchPoint touchPoint) {
            super();
            this.setLayoutX(DrawWire.this.getEndX());
            this.setLayoutY(DrawWire.this.getEndY());

            this.touchID = touchPoint == null ? -1 : touchPoint.getId();
            this.dragStarted = true;
            this.nearbyAnchors = new ArrayList<>();
            this.lastNearbyUpdate = Point2D.ZERO;

            this.disapperance = new Timeline(new KeyFrame(Duration.millis(2000),
                    e -> DrawWire.this.remove(),
                    new KeyValue(this.opacityProperty(), 0.3),
                    new KeyValue(DrawWire.this.opacityProperty(), 0.2)));

            // a circle with hole is built from a path of round arcs with a very thick stroke
            ArcTo arc1 = new ArcTo(100, 100, 0, 100, 0, true, true);
            ArcTo arc2 = new ArcTo(100, 100, 0, -100, 0, true, true);
            this.getElements().addAll(new MoveTo(-100, 0), arc1, arc2, new ClosePath());
            this.setStroke(Color.web("#0066FF"));
            this.setStrokeType(StrokeType.INSIDE);
            this.setStrokeWidth(90);
            this.setStroke(Color.web("#0066FF"));
            this.setStrokeType(StrokeType.INSIDE);
            this.setOpacity(0);

            if (touchPoint != null) {
                touchPoint.grab(this);
            }

            this.addEventHandler(TouchEvent.TOUCH_PRESSED, this::handleTouchPress);
            this.addEventHandler(TouchEvent.TOUCH_MOVED, this::handleTouchDrag);
            this.addEventHandler(TouchEvent.TOUCH_RELEASED, this::handleTouchRelease);
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePress);
            this.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDrag);
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseRelease);
        }

        private void handleReleaseOnNothing() {
            this.makeVisible();
            disapperance.playFromStart();
        }

        private void handleReleaseOnSelf() {
            this.makeVisible();
            disapperance.playFrom(disapperance.getTotalDuration().multiply(0.9));
        }

        private void makeVisible() {
            this.clearWireReactions();
            this.setScaleX(0.25);
            this.setScaleY(0.25);
            this.setOpacity(0.6);
            this.setStrokeWidth(99);
            DrawWire.this.setOpacity(1);
            // avoid clicking the hole with a non moving mouse
            this.setLayoutY(this.getLayoutY() + (DrawWire.this.anchor instanceof InputAnchor ? -2 : 2));
        }

        private void clearWireReactions() {
            for (ConnectionAnchor anchor : this.nearbyAnchors) {
                anchor.setNearbyWireReaction(0);
            }
        }

        private void remove() {
            this.clearWireReactions();
            TopLevelPane pane = DrawWire.this.anchor.getTopLevelPane();
            pane.removeUpperTouchArea(this);
        }

        private void handleTouchPress(TouchEvent event) {
            if (!this.dragStarted) {
                this.touchID = event.getTouchPoint().getId();
                this.disapperance.stop();
                this.makeVisible();
            }
            event.consume();
        }

        private void handleMousePress(MouseEvent event) {
            if (event.isSynthesized()) {
                // don't react on synthesized events
            } else if (event.getButton() == MouseButton.PRIMARY) {
                this.touchID = -1;
                this.handleDragStart();
                this.disapperance.stop();
                DrawWire.this.setOpacity(1);
            } else {
                DrawWire.this.remove();
            }
            event.consume();
        }

        private void handleTouchRelease(TouchEvent event) {
            this.dragStarted = false;
            long fingerCount = event.getTouchPoints().stream().filter(tp -> tp.belongsTo(this)).count();

            if (fingerCount == 1 && DrawWire.this.menu == null) {
                Node picked = event.getTouchPoint().getPickResult().getIntersectedNode();
                DrawWire.this.handleReleaseOn(picked);
            } else if (DrawWire.this.menu != null || this.touchID < 0) {
                // avoid accidental creation of (more) menus
            } else if (fingerCount == 2) {
                DrawWire.this.showMenu(false);
                // a delay to avoid the background picking up jitter from this event
                Timeline delay = new Timeline(new KeyFrame(Duration.millis(250), e -> this.makeVisible()));
                delay.play();
            }

            event.consume();
        }

        private void handleMouseRelease(MouseEvent event) {
            if (event.isSynthesized()) {
                // don't react on synthesized events
            } else if (DrawWire.this.menu != null) {
                // release has no effect if there is a menu
            } else if (event.getButton() == MouseButton.PRIMARY) {
                DrawWire.this.handleReleaseOn(event.getPickResult().getIntersectedNode());
                this.dragStarted = false;
            } else {
                DrawWire.this.showMenu(true);
                this.dragStarted = false;
                this.makeVisible();
            }
            event.consume();
        }

        private void handleTouchDrag(TouchEvent event) {
            double scaleFactor = this.getScaleX();
            double deltaX = event.getTouchPoint().getX() * scaleFactor;
            double deltaY = event.getTouchPoint().getY() * scaleFactor;

            if (event.getTouchPoint().getId() != this.touchID) {
                // we use only primary finger for drag movement
                if (this.dragStarted && Math.abs(deltaX) > 175) {
//                    this.horizontalSplittingAction(event);
                } else if (this.dragStarted && Math.abs(deltaY) > 150) {
//                    this.verticalSplittingAction(event);
                }
            } else {

                if ((deltaX * deltaX + deltaY * deltaY) > 10000) {
                    // FIXME: ignore too large movements
                } else if (this.dragStarted || (deltaX * deltaX + deltaY * deltaY) > 63) {
                    if (!this.dragStarted) {
                        this.handleDragStart();
                    }

                    double newX = this.getLayoutX() + deltaX;
                    double newY = this.getLayoutY() + deltaY;
                    this.dragTo(newX, newY, event.getTouchPoint().getPickResult().getIntersectedNode());
                }
            }

            event.consume();
        }

        private void handleMouseDrag(MouseEvent event) {
            if (DrawWire.this.menu == null && !event.isSynthesized()) {
                double scaleFactor = this.getScaleX();
                double newX = this.getLayoutX() + event.getX() * scaleFactor;
                double newY = this.getLayoutY() + event.getY() * scaleFactor;
                this.dragTo(newX, newY, event.getPickResult().getIntersectedNode());
            }
            event.consume();
        }

        private void dragTo(double newX, double newY) {
            this.dragTo(newX, newY, null);
        }

        private void dragTo(double newX, double newY, Node picked) {
            this.setLayoutX(newX);
            this.setLayoutY(newY);
            Point2D newPos = new Point2D(newX, newY);
            DrawWire.this.setFreePosition(newPos);

            // threshold to avoid doing a quite expensive computation too often
            if (this.lastNearbyUpdate.distance(newPos) > 10) {
                this.lastNearbyUpdate = newPos;
                List<ConnectionAnchor> targetAnchors = anchor.getBlock().getTopLevelPane().allNearbyFreeAnchors(newPos, 166);
                List<ConnectionAnchor> newNearby = new ArrayList<>();

                // trial unification on all nearby opposite free anchor so see if they could fit
                if (DrawWire.this.anchor instanceof InputAnchor) {
                    InputAnchor anchor = (InputAnchor) DrawWire.this.anchor;
                    ConnectionAnchor releaseAnchor = DrawWire.findPickedAnchor(picked);
                    if (releaseAnchor == anchor) {
                        releaseAnchor = null;
                    }

                    for (ConnectionAnchor target : targetAnchors) {
                        if (target instanceof OutputAnchor) {
                            target.setNearbyWireReaction(determineWireReaction((OutputAnchor<?>) target, anchor));
                            newNearby.add(target);
                        }
                    }
                } else {
                    OutputAnchor anchor = (OutputAnchor) DrawWire.this.anchor;
                    ConnectionAnchor releaseAnchor = DrawWire.findPickedAnchor(picked);
                    if (releaseAnchor == anchor) {
                        releaseAnchor = null;
                    }

                    for (ConnectionAnchor target : targetAnchors) {
                        if (target instanceof InputAnchor) {
                            newNearby.add(target);
                            target.setNearbyWireReaction(determineWireReaction(anchor, (InputAnchor) target));
                        }
                    }
                }

                // reset all anchors that are not nearby anymore
                for (ConnectionAnchor oldNear : this.nearbyAnchors) {
                    if (!newNearby.contains(oldNear)) {
                        oldNear.setNearbyWireReaction(GOOD_TYPE_REACTION);
                    }
                }

                this.nearbyAnchors = newNearby;
            }
        }

        private int determineWireReaction(OutputAnchor source, InputAnchor sink) {

            if (source.getType() == sink.getType()) {
                if (source.getBlock() == sink.getBlock()) {
                    return NEUTRAL_TYPE_REACTION;
                }
                return GOOD_TYPE_REACTION;
            } else return WRONG_TYPE_REACTION;

        }

        private void handleDragStart() {
            this.dragStarted = true;
            if (DrawWire.this.menu != null) {
                // resume dragging the wire
                DrawWire.this.menu.close();
                DrawWire.this.menu = null;
            }

            this.setScaleX(1);
            this.setScaleY(1);
            this.setOpacity(0);
            this.setStrokeWidth(90);
        }

    }
}
