package io.metjka.vortex.ui;

public enum Type {

    ARRAY,
    IMAGE,
    NUMBER,
    COLOR;

    public static <T extends Number> String NumberToString(T numer) {
        return numer.toString();
    }


}
