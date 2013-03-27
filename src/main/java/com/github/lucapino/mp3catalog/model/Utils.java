/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lucapino.mp3catalog.model;

/**
 *
 * @author tagliani
 */
public class Utils {

    public static String formatDuration(Long duration) {
        return (duration == null ? "00:00" : String.format("%d:%02d:%02d",
                duration / 3600,
                duration % 3600 / 60,
                duration % 60));
    }
}
