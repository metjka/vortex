package io.metjka.vortex.ui.connections

import javafx.beans.property.ObjectPropertyBase

/**
 * Created by Ihor Salnikov on 4.3.2017, 1:05 PM.
 * https://github.com/metjka/VORT
 */
class Prop<T> : ObjectPropertyBase<T>() {

    var obj: T? = null

    override fun setValue(value: T) {
        obj = value
        fireValueChangedEvent()
    }

    override fun getName(): String {
        return ""
    }

    override fun getBean(): Any {
        return obj!!
    }

    override fun get(): T {
        return obj!!
    }

}
