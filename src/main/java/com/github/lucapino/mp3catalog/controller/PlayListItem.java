/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lucapino.mp3catalog.controller;

/**
 *
 * @author Tagliani
 */
public class PlayListItem {

    private String title;
    private String path;

    public PlayListItem(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    
    @Override
    public String toString() {
        return title;
    }
}
