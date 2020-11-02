/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ml.tools;

import static org.ml.tools.DataKind.*;

/**
 *
 * @author osboxes
 */
public enum DataType {

    TypeString(StringKind, ""),
    TypeInteger(NumericKind, 0),
    TypeEmail(StringKind, ""),
    TypeBoolean(BooleanKind, false),
    TypeDouble(NumericKind, 0.0d),
    TypeIntegerPercentage(NumericKind, 0),
    TypeDoublePercentage(NumericKind, 0.0d),
    TypeUndefined(StringKind, "Undefined");

    Comparable defaultValue;
    DataKind dataKind;

    /**
     *
     * @return
     */
    public Comparable getDefaultValue() {
        return defaultValue;
    }

    /**
     *
     * @return
     */
    public DataKind getDataKind() {
        return dataKind;
    }

    /**
     *
     * @param defaultValue
     */
    DataType(DataKind dataKind, Comparable defaultValue) {
        this.dataKind = dataKind;
        this.defaultValue = defaultValue;
    }
}
