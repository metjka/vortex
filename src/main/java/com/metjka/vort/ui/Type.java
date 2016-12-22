package com.metjka.vort.ui;

public enum Type {

    ARRAY,
    IMAGE,
    NUMBER,
    COLOR;

    public static <T extends Number> String NumberToString(T numer){
        return numer.toString();
    }

}
