package it.mp3catalog.view;

import ca.odell.glazedlists.TextFilterator;
import it.mp3catalog.model.Song;
import java.util.List;

/**
 * Get the Strings to filter against for a given Issue.
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class SongTextFilterator implements TextFilterator<Song> {

    @Override
    public void getFilterStrings(List<String> baseList, Song song) {
        baseList.add(song.getAlbum());
        baseList.add(song.getArtist());
        baseList.add(song.getFileName());
        baseList.add(song.getLyrics());
        baseList.add(song.getTitle());
    }
}