/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lucapino.catalog.controller;

import com.github.lucapino.catalog.model.Song;

/**
 *
 * @author Tagliani
 */
public class PlayListItem {

    private String title;
    private String path;
    private Song song;

    public PlayListItem(Song song, String title, String path) {
        this.title = title;
        this.path = path;
        this.song = song;
    }

    public Song getSong() {
        return song;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return title;
    }
}
