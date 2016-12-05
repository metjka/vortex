package com.metjka.vort.ui;

import java.util.Map;

/**
 * Created by isalnikov on 12/5/2016.
 */
public interface Bundleable {
    /**
     * String used in serialization to indicate type of object in the serialized file
     */
    String KIND = "kind";

    /**
     * Serialization function.
     *
     * @return a map that describes the bundleable object.
     */
    Map<String, Object> toBundle();
}
