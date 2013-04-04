/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lucapino.catalog.controller;

import ca.odell.glazedlists.gui.TableFormat;
import com.github.lucapino.catalog.model.Song;
import java.util.ResourceBundle;

/**
 *
 * @author Tagliani
 */
public class SongTableFormat implements TableFormat<Song> {

    private ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    @Override
    public int getColumnCount() {
        return 10;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return bundle.getString("TITLE");
        } else if (column == 1) {
            return bundle.getString("ARTIST");
        } else if (column == 2) {
            return bundle.getString("ALBUM");
        } else if (column == 3) {
            return bundle.getString("DURATION");
        } else if (column == 4) {
            return bundle.getString("FILENAME");
        } else if (column == 5) {
            return bundle.getString("SIZE");
        } else if (column == 6) {
            return bundle.getString("BITRATE");
        } else if (column == 7) {
            return bundle.getString("TRACK NUMBER");
        } else if (column == 8) {
            return bundle.getString("FILETYPE");
        } else if (column == 9) {
            return bundle.getString("RATING");
        }
        throw new IllegalStateException();
    }

    @Override
    public Object getColumnValue(Song song, int column) {
        Object result;
        switch (column) {
            case 0:
                result = song.getTitle();
                break;
            case 1:
                result = song.getArtist();
                break;
            case 2:
                result = song.getAlbum();
                break;
            case 3:
                result = song.getDuration();
                break;
            case 4:
                result = song.getFileName();
                break;
            case 5:
                result = song.getFileSize();
                break;
            case 6:
                result = song.getBitrate();
                break;
            case 7:
                result = song.getTrackNumber();
                break;
            case 8:
                result = song.getFileType();
                break;
            case 9:
                result = song.getRating();
                break;
            default:
                throw new IllegalStateException();
        }
        return result;
    }
}
