/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lucapino.catalog.model;

/**
 *
 * @author tagliani
 */
public class Utils {

    public static String formatDuration(int duration) {
        return (duration < 0 ? "00:00" : String.format("%d:%02d:%02d",
                duration / 3600,
                duration % 3600 / 60,
                duration % 60));
    }
}
