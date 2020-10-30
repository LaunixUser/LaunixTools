/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ml.tools;

/**
 *
 * @author osboxes
 */
public enum DataType {

    TypeString(""),
    TypeInteger(0),
    TypeEmail(""),
    TypeBoolean(false),
    TypeDouble(0.0d),
    TypeIntegerPercentage(0),
    TypeDoublePercentage(0.0d),
    TypeUndefined("Undefined");

    Comparable defaultValue;

    /**
     *
     * @return
     */
    public Comparable getDefaultValue() {
        return defaultValue;
    }

    /**
     *
     * @param defaultValue
     */
    DataType(Comparable defaultValue) {
        this.defaultValue = defaultValue;
    }
}
