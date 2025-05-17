package com.oasisstudios.camelupadmin.gui;

import javafx.scene.paint.Color;


/**
 * The Color converter.
 */
public final class ColorConverter {

    /**
     * Color to hex string.
     *
     * @param color the color
     * @return the string
     */
    static public String ColorToHex(Color color) {
        return "#"+color.toString().substring(2, 8).toUpperCase();
    }
}
