package com.metjka.vort.ui.components;

import com.metjka.vort.ui.components.connections.ConnectionAnchor;

/** Helper interface for finding the associated connection anchor on release a wire onto something. */
public interface Target {
    /** @return the connection anchor directly related to the Target object. */
    ConnectionAnchor getAssociatedAnchor();
}
